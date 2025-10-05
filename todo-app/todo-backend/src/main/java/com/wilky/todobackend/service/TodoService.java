package com.wilky.todobackend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wilky.todobackend.dto.TodoDTO;
import com.wilky.todobackend.model.Todo;
import com.wilky.todobackend.repository.TodoRepository;

@Service
public class TodoService {

	private TodoRepository todoRepository;

	public TodoService(TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}

	public Todo save(TodoDTO todoDTO) {
		Todo todo = new Todo();
		todo.setName(todoDTO.getName());
		todo.setCreatedAt(LocalDateTime.now());
		return this.todoRepository.save(todo);
	}

	public List<TodoDTO> getAllTodos() {
		return this.todoRepository.findAll().stream().map(todo -> {
			TodoDTO todoDTO = new TodoDTO();
			todoDTO.setId(todo.getId());
			todoDTO.setName(todo.getName());
			todoDTO.setDone(todo.isDone());
			return todoDTO;
		}).toList();
	}

	public Todo markedAsDone(long id) {
		Todo todo = this.todoRepository.findById(id).get();
		todo.setDone(true);
		todo.setUpdateAt(LocalDateTime.now());
		return this.todoRepository.save(todo);
	}
}
