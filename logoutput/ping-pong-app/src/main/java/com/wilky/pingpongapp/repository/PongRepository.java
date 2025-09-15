package com.wilky.pingpongapp.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.wilky.pingpongapp.model.Pong;

@Repository
public interface PongRepository extends CrudRepository<Pong, Long> {

	List<Pong> findAll();
}