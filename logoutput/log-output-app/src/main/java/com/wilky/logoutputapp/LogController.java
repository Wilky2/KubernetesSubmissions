package com.wilky.logoutputapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class LogController {

	private final String filePath;
	private final String fileName;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);

	public LogController(@Value("${file.path}") String filePath, @Value("${file.name}") String fileName) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.objectMapper = new ObjectMapper();
	}

	@GetMapping("status")
	public List<CurrentStatus> getCurrentStatus() {
		try {
			logger.info("Reading file :" + this.filePath + "/" + this.fileName);
			File file = Paths.get(filePath, fileName).toFile();
			if (!file.exists()) {
				return Collections.emptyList(); // return [] if file doesn’t exist
			}
			return objectMapper.readValue(file, new TypeReference<List<CurrentStatus>>() {
			});
		} catch (IOException e) {
			throw new RuntimeException("Failed to read status file", e);
		}
	}

}
