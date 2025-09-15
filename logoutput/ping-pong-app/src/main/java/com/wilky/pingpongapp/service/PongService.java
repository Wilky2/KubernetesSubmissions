package com.wilky.pingpongapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wilky.pingpongapp.model.Pong;
import com.wilky.pingpongapp.repository.PongRepository;

@Service
public class PongService {

	private PongRepository pongRepository;

	public PongService(PongRepository pongRepository) {
		super();
		this.pongRepository = pongRepository;
	}

	public Pong save(Pong pong) {
		return this.pongRepository.save(pong);
	}

	public Pong getCurrentPong() {
		List<Pong> pongList = this.pongRepository.findAll();
		if (pongList.size() == 0) {
			var pong = new Pong();
			pong.setCounter(0);
			return pong;
		}
		return pongList.get(0);
	}

}
