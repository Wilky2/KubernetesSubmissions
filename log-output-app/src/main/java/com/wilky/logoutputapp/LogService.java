package com.wilky.logoutputapp;

import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	private static final Logger logger = LoggerFactory.getLogger(LogService.class);

	private String randomString;

	private Timestamp currentTimestamp;

	public LogService() {
		randomString = UUID.randomUUID().toString();
	}

	@Scheduled(fixedRate = 5000)
	public void run() throws InterruptedException {
		logger.info(this.randomString);
		long millis = System.currentTimeMillis();
		this.currentTimestamp = new Timestamp(millis);
	}

	public String getRandomString() {
		return this.randomString;
	}

	public Timestamp getCurrentTimestamp() {
		return currentTimestamp;
	}

}
