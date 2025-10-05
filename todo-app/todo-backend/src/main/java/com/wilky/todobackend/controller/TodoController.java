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

import com.wilky.todobackend.dto.Message;
import com.wilky.todobackend.dto.TodoDTO;
import com.wilky.todobackend.model.Todo;
import com.wilky.todobackend.service.NatsPublisher;
import com.wilky.todobackend.service.TodoService;

@RestController
@RequestMapping("todos")
public class TodoController {

	private final TodoService todoService;

	private final String redirectUrl;

	private final NatsPublisher natsPublisher;

	private final String todoCreatedSubject;
	private final String todoCompletedSubject;

	public TodoController(@Value("${redirect.url}") String redirectUrl, TodoService todoService,
			NatsPublisher natsPublisher, @Value("${subject.created}") String todoCreatedSubject,
			@Value("${subject.completed}") String todoCompletedSubject) {
		this.todoService = todoService;
		this.redirectUrl = redirectUrl;
		this.natsPublisher = natsPublisher;
		this.todoCreatedSubject = todoCreatedSubject;
		this.todoCompletedSubject = todoCompletedSubject;
	}

	Logger log = LoggerFactory.getLogger(getClass());

	@PostMapping
	public RedirectView saveTask(@ModelAttribute TodoDTO todoDTO) {
		String name = todoDTO.getName();
		log.info("Received new Todo: {}", name);
		if (name != null && name.length() > 140) {
			log.warn("Todo not allowed: exceeds 140 characters -> {}", name);
			RedirectView redirectView = new RedirectView(this.redirectUrl);
			redirectView.setStatusCode(HttpStatus.SEE_OTHER);
			return redirectView;
		}
		Todo todo = this.todoService.save(todoDTO);
		Message message = new Message();
		message.setMessage("Todo created");
		message.setTodo(todo);
		natsPublisher.publish(this.todoCreatedSubject, message);
		log.info("Redirect to " + this.redirectUrl);
		RedirectView redirectView = new RedirectView(this.redirectUrl);
		redirectView.setStatusCode(HttpStatus.SEE_OTHER);
		return redirectView;
	}

	@GetMapping
	public List<TodoDTO> getTasks() {
		return this.todoService.getAllTodos();
	}

	@PutMapping("/{id}")
	public Todo markedAsDone(@PathVariable long id) {
		Todo todo = this.todoService.markedAsDone(id);
		log.info("Todo marked as done " + todo);
		Message message = new Message();
		message.setMessage("Todo marked as done");
		message.setTodo(todo);
		natsPublisher.publish(this.todoCompletedSubject, message);
		return todo;
	}

}
