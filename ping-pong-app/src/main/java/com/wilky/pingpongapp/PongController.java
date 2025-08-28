package com.wilky.pingpongapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PongController {

	long counter = 0;

	@GetMapping("pingpong")
	public String getPong() {
		String response = "pong " + this.counter;
		this.counter++;
		return response;
	}

}
