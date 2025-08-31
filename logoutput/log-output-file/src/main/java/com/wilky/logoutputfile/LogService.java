package com.wilky.logoutputfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	public void run() throws IOException {
		Map<String, Object> newEntry = buildEntry();
		writeData(newEntry);
	}

	private Map<String, Object> buildEntry() {
		long millis = System.currentTimeMillis();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		Timestamp currentTimestamp = new Timestamp(millis);
		String formatted = currentTimestamp.toInstant().atZone(ZoneId.systemDefault()).format(formatter);

		Map<String, Object> entry = new LinkedHashMap<>();
		entry.put("randomString", this.randomString);
		entry.put("timestamp", formatted);

		logger.info("Building new entry: " + entry);
		return entry;
	}

	private void writeData(Map<String, Object> entry) throws IOException {
		Files.createDirectories(Paths.get(filePath));

		File file = Paths.get(filePath, fileName).toFile();
		objectMapper.writeValue(file, entry);

		logger.info("Written entry to " + file.getAbsolutePath());
	}

}