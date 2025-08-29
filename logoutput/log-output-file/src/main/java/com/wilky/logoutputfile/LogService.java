package com.wilky.logoutputfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class LogService {

	private final String randomString;
	private final String filePath;
	private final String fileName;
	private final ObjectMapper objectMapper;
	private static final Logger logger = LoggerFactory.getLogger(LogService.class);

	public LogService(@Value("${file.path}") String filePath, @Value("${file.name}") String fileName) {
		randomString = UUID.randomUUID().toString();
		this.filePath = filePath;
		this.fileName = fileName;
		this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	}

	public void run() throws InterruptedException, StreamReadException, DatabindException, IOException {

		long millis = System.currentTimeMillis();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		Timestamp currentTimestamp = new Timestamp(millis);
		String formatted = currentTimestamp.toInstant().atZone(ZoneId.systemDefault()).format(formatter);

		// File reference
		File file = Paths.get(filePath, fileName).toFile();

		// Ensure directory exists
		Files.createDirectories(Paths.get(filePath));

		List<Map<String, String>> logs;

		if (file.exists()) {
			// Load existing JSON array
			logs = objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {
			});
		} else {
			logs = new ArrayList<>();
		}

		// Add new log entry
		Map<String, String> entry = new LinkedHashMap<>();
		entry.put("randomString", randomString);
		entry.put("timestamp", formatted);
		logs.add(entry);

		logger.info("Adding " + entry + " in " + this.filePath + "/" + this.fileName);

		// Write back to JSON file
		objectMapper.writeValue(file, logs);
	}

}
