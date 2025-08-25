package com.wilky.todoappwebserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	private String port;
	
	

	public HomeController(@Value("${server.port}") String port) {
		super();
		this.port = port;
	}


	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("port", this.port);
		return "index";
	}
	
}
