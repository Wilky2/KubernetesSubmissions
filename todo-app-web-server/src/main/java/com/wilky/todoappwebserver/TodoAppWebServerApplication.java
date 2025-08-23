package com.wilky.todoappwebserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;


@SpringBootApplication
public class TodoAppWebServerApplication {

	private static final Logger logger = LoggerFactory.getLogger(TodoAppWebServerApplication.class);
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TodoAppWebServerApplication.class, args);
		ConfigurableEnvironment env = context.getEnvironment();
		String port = env.getProperty("server.port");
		logger.info("Server started in port " + port);
	}

}
