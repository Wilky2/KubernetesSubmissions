package com.wilky.logoutputapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LogController {

	private final String filePath;
	private final String fileName;
	private final String pingpong;
	private final String pingpongUrl;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);

	public LogController(@Value("${file.path}") String filePath, @Value("${file.name}") String fileName,
			@Value("${file.pingpong}") String pingpong, @Value("${pingpong.url}") String pingpongUrl) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.pingpong = pingpong;
		this.pingpongUrl = pingpongUrl;
		this.restTemplate = new RestTemplate();
		this.objectMapper = new ObjectMapper();
	}

	@GetMapping("status")
	public String getCurrentStatus() {
		CurrentStatus status = readJsonStatus();
		long pingpongValue = this.fetchPingpongValue();
		status.setPingpong(pingpongValue);
		return formatStatus(status);
	}

	private long fetchPingpongValue() {
		try {
			Long response = restTemplate.getForObject(this.pingpongUrl, Long.class);
			return response;
		} catch (Exception e) {
			logger.error("Erreur lors de la récupération du pingpong depuis {}", pingpongUrl, e);
		}
		return 0;
	}

	private CurrentStatus readJsonStatus() {
		File file = Paths.get(filePath, fileName).toFile();
		logger.info("Reading file :" + file.getAbsolutePath());

		if (!file.exists()) {
			return new CurrentStatus();
		}

		try {
			return objectMapper.readValue(file, new TypeReference<CurrentStatus>() {
			});
		} catch (IOException e) {
			throw new RuntimeException("Failed to read status file", e);
		}
	}

	private long readPingpongValue() {
		File pingpongFile = Paths.get(filePath, pingpong).toFile();

		if (!pingpongFile.exists()) {
			return 0;
		}

		try {
			String value = new String(Files.readAllBytes(pingpongFile.toPath())).trim();
			return Long.parseLong(value);
		} catch (Exception e) {
			logger.warn("Failed to read pingpong file, defaulting to 0", e);
			return 0;
		}
	}

	private String formatStatus(CurrentStatus status) {
		return String.format("%s: %s.</br>Ping / Pongs: %d", status.getTimestamp(), status.getRandomString(),
				status.getPingpong());
	}

}
