package com.wilky.broadcast.model;

import lombok.Data;

@Data
public class Todo {
	private Long id;
	private String name;
	private Long userId;
	private String createdAt;
	private String updateAt;
	private boolean done;

}
