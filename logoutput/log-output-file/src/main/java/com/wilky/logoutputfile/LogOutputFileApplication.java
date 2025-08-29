package com.wilky.logoutputfile;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogOutputFileApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(LogOutputFileApplication.class, args);
		var logService = context.getBean(LogService.class);
		while (true) {
			try {
				logService.run();
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
