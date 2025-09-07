package com.wilky.todoappwebserver.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RandomImageService {

	private static final Logger logger = Logger.getLogger(RandomImageService.class.getName());

	private final Path dirPath;
	private final String imageBaseName;
	private final HttpClient http;

	private final Random random = new Random();

	public RandomImageService(@Value("${file.path}") String dirPath, @Value("${file.img-name}") String imageBaseName) {
		this.dirPath = Paths.get(dirPath);
		this.imageBaseName = imageBaseName;
		// On ne suit PAS automatiquement les redirections (on veut lire Location
		// nous-mêmes)
		this.http = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER)
				.connectTimeout(Duration.ofSeconds(15)).build();
	}

	// 1) Au démarrage : s'assurer qu'une image existe
	@EventListener(ApplicationReadyEvent.class)
	public void ensureImageOnStartup() {
		try {
			Files.createDirectories(dirPath);
			if (!hasExistingImage()) {
				logger.info("No existing image found at startup. Fetching one now...");
				retryUntilSuccess(this::fetchAndSaveOnce);
			} else {
				logger.info("Existing image found at startup. Skipping initial fetch.");
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to ensure image directory exists", e);
		}
	}

	// 2) Toutes les 10 minutes : rafraîchir l'image aléatoire
	@Scheduled(fixedDelayString = "PT10M", initialDelayString = "PT10M")
	public void fetchPeriodically() {
		retryUntilSuccess(this::fetchAndSaveOnce);
	}

	// --------- cœur du processus : un essai unique (throw si échec) ---------
	private void fetchAndSaveOnce() throws Exception {
		// Étape A : appel initial pour récupérer la redirection (302 + Location)
		int size = generateRandomSize();
		URI initial = URI.create("https://picsum.photos/" + size);
		logger.info("Generated random image size: " + size);
		HttpRequest req = HttpRequest.newBuilder(initial).GET().timeout(Duration.ofSeconds(20)).build();

		HttpResponse<Void> headResp = http.send(req, HttpResponse.BodyHandlers.discarding());
		if (headResp.statusCode() != 302) {
			throw new IllegalStateException("Expected 302 from picsum, got " + headResp.statusCode());
		}

		String location = Optional.ofNullable(headResp.headers().firstValue("location").orElse(null))
				.orElseThrow(() -> new IllegalStateException("Missing Location header on 302 response"));

		URI downloadUri = initial.resolve(location);
		logger.info("Redirected to: " + downloadUri);

		// Étape B : télécharger l'image (doit renvoyer 200)
		HttpRequest downloadReq = HttpRequest.newBuilder(downloadUri).GET().timeout(Duration.ofSeconds(60)).build();

		HttpResponse<byte[]> imgResp = http.send(downloadReq, HttpResponse.BodyHandlers.ofByteArray());
		if (imgResp.statusCode() != 200) {
			throw new IllegalStateException("Expected 200 when downloading image, got " + imgResp.statusCode());
		}

		// Étape C : déterminer l’extension à partir du Content-Type
		String contentType = imgResp.headers().firstValue("content-type").orElse("").toLowerCase(Locale.ROOT);
		String ext = extensionFromContentType(contentType)
				.orElseGet(() -> extensionFromUrl(downloadUri.toString()).orElse("img"));

		// Étape D : supprimer l’ancienne image (quel que soit l’extension), puis
		// sauvegarder la nouvelle
		deleteExistingImages();
		Path out = dirPath.resolve(imageBaseName + "." + ext);
		try (OutputStream os = Files.newOutputStream(out, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING)) {
			os.write(imgResp.body());
		}
		logger.info("Saved new image to: " + out.toAbsolutePath());
	}

	// --------- utilitaires ---------

	private int generateRandomSize() {
		// Par exemple entre 400 et 1600 pixels
		int min = 0;
		int max = 5000;
		return random.nextInt(max - min + 1) + min;
	}

	private void retryUntilSuccess(ThrowingRunnable action) {
		while (true) {
			try {
				action.run();
				return; // succès → on sort
			} catch (Exception ex) {
				logger.warning("Image fetch failed: " + ex.getMessage() + ". Retrying in 5 seconds...");
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("Retry interrupted", ie);
				}
			}
		}
	}

	private boolean hasExistingImage() throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, imageBaseName + ".*")) {
			for (Path ignored : stream) {
				return true; // il y a au moins un fichier
			}
			return false;
		}
	}

	private void deleteExistingImages() throws IOException {
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, imageBaseName + ".*")) {
			for (Path p : stream) {
				Files.deleteIfExists(p);
				logger.info("Deleted old image: " + p.toAbsolutePath());
			}
		}
	}

	private Optional<String> extensionFromContentType(String ct) {
		if (ct == null)
			return Optional.empty();
		if (ct.contains("image/jpeg") || ct.contains("image/jpg"))
			return Optional.of("jpg");
		if (ct.contains("image/png"))
			return Optional.of("png");
		if (ct.contains("image/webp"))
			return Optional.of("webp");
		if (ct.contains("image/gif"))
			return Optional.of("gif");
		if (ct.contains("image/bmp"))
			return Optional.of("bmp");
		if (ct.contains("image/svg"))
			return Optional.of("svg");
		return Optional.empty();
	}

	private Optional<String> extensionFromUrl(String url) {
		int q = url.indexOf('?');
		String clean = q >= 0 ? url.substring(0, q) : url;
		int dot = clean.lastIndexOf('.');
		if (dot > clean.lastIndexOf('/')) {
			String ext = clean.substring(dot + 1).toLowerCase(Locale.ROOT);
			// sanity check (limiter à des extensions communes)
			if (ext.matches("[a-z0-9]{2,5}"))
				return Optional.of(ext);
		}
		return Optional.empty();
	}

	@FunctionalInterface
	private interface ThrowingRunnable {
		void run() throws Exception;
	}
}
