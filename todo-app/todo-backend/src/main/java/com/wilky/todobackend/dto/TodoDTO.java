package com.wilky.todobackend.dto;

import lombok.Data;

@Data
public class TodoDTO {

	private long id;

	private String name;

	private boolean isDone;

}
