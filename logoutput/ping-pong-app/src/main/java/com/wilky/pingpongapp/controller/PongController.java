package com.wilky.pingpongapp.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wilky.pingpongapp.model.Pong;
import com.wilky.pingpongapp.service.PongService;

@RestController
public class PongController {

	private Pong counter;

	private final String filePath;
	private final String fileName;

	private static final Logger logger = LoggerFactory.getLogger(PongController.class);

	private PongService pongService;

	public PongController(@Value("${file.path}") String filePath, @Value("${file.pingpong}") String fileName,
			PongService pongService) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.pongService = pongService;
		this.counter = this.pongService.getCurrentPong();
	}

	@GetMapping("pingpong")
	public String getPong() throws IOException {
		this.counter.setCounter(this.counter.getCounter() + 1);
		this.counter = this.pongService.save(this.counter);
		String response = "pong " + this.counter.getCounter();
		return response;
	}

	@GetMapping("value")
	public long getValue() throws IOException {
		return this.counter.getCounter();
	}

	private void writeData(long counter) throws IOException {
		Files.createDirectories(Paths.get(filePath));

		File file = Paths.get(this.filePath, this.fileName).toFile();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(String.valueOf(counter));
		}

		logger.info("Written counter " + counter + " to " + file.getAbsolutePath());
	}

	private long readPingpongValue() {
		File pingpongFile = Paths.get(filePath, this.fileName).toFile();

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

}
