package com.wilky.todoappwebserver.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageController {

	private final Path dirPath;
	private final String imgName;

	public ImageController(@Value("${img.path}") String filePath, @Value("${img.name}") String imgName) {
		this.dirPath = Paths.get(filePath);
		this.imgName = imgName;
	}

	@GetMapping("/image/current")
	public ResponseEntity<Resource> getCurrentImage() throws IOException {
		// Cherche le fichier existant (quelle que soit l’extension)
		Path imagePath = Files.list(dirPath).filter(p -> p.getFileName().toString().startsWith(imgName + "."))
				.findFirst().orElseThrow(() -> new RuntimeException("No current image found"));

		Resource resource = new UrlResource(imagePath.toUri());

		// Détection du type MIME
		String contentType = Files.probeContentType(imagePath);
		if (contentType == null) {
			contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imagePath.getFileName() + "\"")
				.contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
}
