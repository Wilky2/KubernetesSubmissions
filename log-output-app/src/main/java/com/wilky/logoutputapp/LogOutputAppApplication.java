package com.wilky.logoutputapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogOutputAppApplication{

	
	
	public static void main(String[] args) {
		var context = SpringApplication.run(LogOutputAppApplication.class, args);
		var logService = context.getBean(LogService.class);
		try {
			logService.run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
