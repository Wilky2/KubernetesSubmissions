package com.wilky.logoutputapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogOutputAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogOutputAppApplication.class, args);
	}

}
