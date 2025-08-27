package com.wilky.logoutputapp;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

	LogService logService;

	public LogController(LogService logService) {
		super();
		this.logService = logService;
	}

	@GetMapping("status")
	public CurrentStatus getCurrentStatus() {
		var currentStatus = new CurrentStatus();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		String formatted = logService.getCurrentTimestamp().toInstant().atZone(ZoneId.systemDefault())
				.format(formatter);
		currentStatus.setCurrentTimestamp(formatted);
		currentStatus.setRandomString(this.logService.getRandomString());
		return currentStatus;
	}

}
