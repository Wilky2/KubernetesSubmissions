package com.wilky.todoappwebserver.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.wilky.todoappwebserver.model.Todo;

@Controller
public class HomeController {

	private final String backendUrl;
	private final RestTemplate restTemplate;
	private final String formUrl;

	Logger log = LoggerFactory.getLogger(getClass());

	public HomeController(@Value("${todobackend}") String backendUrl, @Value("${form.url}") String formUrl) {
		this.backendUrl = backendUrl;
		this.restTemplate = new RestTemplate();
		this.formUrl = formUrl;
		log.info("Backend url" + this.backendUrl);
		log.info("Form url" + this.formUrl);
	}

	@GetMapping("/")
	public String home(Model model) {
		List<Todo> todos;
		try {
			log.info("Getting the todo list from " + this.backendUrl);
			Todo[] response = restTemplate.getForObject(backendUrl, Todo[].class);
			if (response != null && response.length > 0) {
				todos = Arrays.asList(response);
			} else {
				todos = Collections.emptyList();
			}
		} catch (Exception e) {
			log.error("Error while getting the todo list : " + e.getMessage());
			todos = Collections.emptyList();
		}

		log.info("Todo list :" + todos);

		model.addAttribute("todos", todos);
		model.addAttribute("todo", new Todo());
		model.addAttribute("form_url", this.formUrl);
		model.addAttribute("list_empty", todos.isEmpty());
		return "index";
	}

}
