package com.wilky.todobackend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wilky.todobackend.model.Todo;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {

	List<Todo> findAll();

}
