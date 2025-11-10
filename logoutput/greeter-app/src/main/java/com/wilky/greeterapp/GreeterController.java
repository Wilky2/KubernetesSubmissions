package com.wilky.greeterapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreeterController {

	@Value("${version:unknown}")
	private String version;

	@GetMapping("/greeter")
	public String greet() {
		return "Hello from version " + version;
	}
}
