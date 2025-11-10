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
	private final String pingpongUrl;
	private final String greetingUrl;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);
	private final String configFilePath;
	private final String message;

	public LogController(@Value("${file.path}") String filePath, @Value("${file.name}") String fileName,
			@Value("${pingpong.url}") String pingpongUrl, @Value("${greeting.url}") String greetingUrl,
			@Value("${message}") String message) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.pingpongUrl = pingpongUrl;
		this.greetingUrl = greetingUrl;
		this.restTemplate = new RestTemplate();
		this.objectMapper = new ObjectMapper();
		this.configFilePath = "/usr/src/app/info/information.txt";
		this.message = message;
	}

	@GetMapping("")
	public String testingPurpose() throws IOException {
		return "The app is running";
	}

	@GetMapping("status")
	public String getCurrentStatus() throws IOException {
		CurrentStatus status = readJsonStatus();
		long pingpongValue = this.fetchPingpongValue();
		String greetingMessage = this.fetchGreetingMessage();

		status.setPingpong(pingpongValue);
		return formatStatus(status, greetingMessage);
	}

	private long fetchPingpongValue() {
		try {
			Long response = restTemplate.getForObject(this.pingpongUrl, Long.class);
			return response != null ? response : 0;
		} catch (Exception e) {
			logger.error("❌ Error fetching pingpong from {}", pingpongUrl, e);
			return 0;
		}
	}

	private String fetchGreetingMessage() {
		try {
			return restTemplate.getForObject(this.greetingUrl, String.class);
		} catch (Exception e) {
			logger.error("⚠️ Failed to fetch greeting from {}", greetingUrl, e);
			return "Unavailable";
		}
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

	private String formatStatus(CurrentStatus status, String greetingMessage) throws IOException {
		logger.info("Reading file content from " + this.configFilePath);
		String content = Files.readString(Paths.get(this.configFilePath));

		return String.format("""
				file content: %s</br>
				env variable: MESSAGE=%s</br>
				%s: %s.</br>
				Ping / Pongs: %d</br>
				greetings: %s
				""", content, this.message, status.getTimestamp(), status.getRandomString(), status.getPingpong(),
				greetingMessage);
	}

}
