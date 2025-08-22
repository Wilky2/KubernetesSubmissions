package com.wilky.logoutputapp;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService{

	private static final Logger logger = LoggerFactory.getLogger(LogService.class);
	
	public void run() throws InterruptedException {
		String randomString = UUID.randomUUID().toString();
		while(true) {
			logger.info(randomString);
			Thread.sleep(5000);
		}
	}

}
