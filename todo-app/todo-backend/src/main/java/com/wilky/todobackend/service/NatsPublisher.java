package com.wilky.todobackend.service;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.nats.client.Connection;
import io.nats.client.Nats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilky.todobackend.dto.Message;

@Component
public class NatsPublisher {

	private static final Logger log = LoggerFactory.getLogger(NatsPublisher.class);
	private final Connection natsConnection;
	private final ObjectMapper objectMapper;

	public NatsPublisher(@Value("${nats.host}") String natsHost, ObjectMapper objectMapper) throws Exception {
		this.objectMapper = objectMapper;
		this.natsConnection = Nats.connect(natsHost);
		log.info("Connected to NATS");
	}

	public void publish(String subject, Message message) {
		try {
			String json = this.objectMapper.writeValueAsString(message);
            log.info("Publishing to [{}]: {}", subject, json);
            natsConnection.publish(subject, json.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			log.error("Failed to publish to NATS: {}", e.getMessage(), e);
		}
	}
}
