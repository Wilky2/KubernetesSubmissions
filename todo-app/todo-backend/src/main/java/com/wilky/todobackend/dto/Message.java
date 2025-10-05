package com.wilky.todobackend.dto;

import com.wilky.todobackend.model.Todo;

import lombok.Data;

@Data
public class Message {

	private String message;
	private Todo todo;

}