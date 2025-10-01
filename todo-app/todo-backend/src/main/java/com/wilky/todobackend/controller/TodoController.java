package com.wilky.todobackend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.wilky.todobackend.model.Todo;
import com.wilky.todobackend.service.TodoService;

@RestController
@RequestMapping("todos")
public class TodoController {

	private final TodoService todoService;

	private final String redirectUrl;

	public TodoController(@Value("${redirect.url}") String redirectUrl, TodoService todoService) {
		this.todoService = todoService;
		this.redirectUrl = redirectUrl;
	}

	Logger log = LoggerFactory.getLogger(getClass());

	@PostMapping
	public RedirectView saveTask(@ModelAttribute Todo todo) {
		String name = todo.getName();
		log.info("Received new Todo: {}", name);
		if (name != null && name.length() > 140) {
			log.warn("Todo not allowed: exceeds 140 characters -> {}", name);
			RedirectView redirectView = new RedirectView(this.redirectUrl);
			redirectView.setStatusCode(HttpStatus.SEE_OTHER);
			return redirectView;
		}
		this.todoService.save(todo);
		log.info("Redirect to " + this.redirectUrl);
		RedirectView redirectView = new RedirectView(this.redirectUrl);
		redirectView.setStatusCode(HttpStatus.SEE_OTHER);
		return redirectView;
	}

	@GetMapping
	public List<Todo> getTasks() {
		return this.todoService.getAllTodos();
	}

	@PutMapping("/{id}")
	public Todo markedAsDone(@PathVariable long id) {
		return this.todoService.markedAsDone(id);
	}

}
