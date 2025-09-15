package com.wilky.todobackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wilky.todobackend.model.Todo;
import com.wilky.todobackend.repository.TodoRepository;

@Service
public class TodoService {

	private TodoRepository todoRepository;

	public TodoService(TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}

	public Todo save(Todo todo) {
		return this.todoRepository.save(todo);
	}

	public List<Todo> getAllTodos() {
		return this.todoRepository.findAll();
	}
}
