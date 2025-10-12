package com.wilky.broadcast.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilky.broadcast.model.Message;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;

@Component
public class NatsBroadcastService {

	private static final Logger log = LoggerFactory.getLogger(NatsBroadcastService.class);

	private final String nats_url;
	private Connection natsConnection;

	private final String discordWebhookUrl;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public NatsBroadcastService(@Value("${nats.url}") String nats_url,
			@Value("${discordwebhookurl}") String discordWebhookUrl,
			@Value("${subject.created}") String todoCreatedSubject,
			@Value("${subject.completed}") String todoCompletedSubject) throws Exception {
		this.nats_url = nats_url;
		this.discordWebhookUrl = discordWebhookUrl;
		Options options = new Options.Builder().server(this.nats_url).connectionName("broadcast-service").build();
		this.natsConnection = Nats.connect(options);
		log.info("✅ Connected to NATS at " + this.nats_url);
		subscribeToSubject(todoCreatedSubject, "broadcast-queue");
		subscribeToSubject(todoCompletedSubject, "broadcast-queue");
		if (isDiscordEnabled()) {
			log.info("🔗 Discord Webhook configured — messages will be sent to Discord");
		} else {
			log.warn("⚠️ Discord Webhook not defined — messages will NOT be sent to Discord");
		}
	}

	private boolean isDiscordEnabled() {
		return discordWebhookUrl != null && !discordWebhookUrl.trim().isEmpty();
	}

	private void subscribeToSubject(String subject, String queueGroup) throws Exception {
		Dispatcher dispatcher = natsConnection.createDispatcher(this::handleMessage);
		dispatcher.subscribe(subject, queueGroup);
		log.info("👂 Listening to subject: " + subject + " in queue group: " + queueGroup);
	}

	private void handleMessage(io.nats.client.Message natsMsg) {
		try {
			String data = new String(natsMsg.getData(), StandardCharsets.UTF_8);
			Message message = objectMapper.readValue(data, Message.class);
			log.info("📥 Received " + message);
			if (isDiscordEnabled()) {
				String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(message.getTodo());
				String discordText = "**" + message.getMessage() + "**\n\n```json\n" + prettyJson + "\n```";
				sendToDiscord(discordText);
			} else {
				log.info("💬 Discord disabled — skipping message broadcast: {}", message.getMessage());
			}
		} catch (Exception e) {
			log.error("Failed to process NATS message", e);
		}
	}

	private void sendToDiscord(String content) {
		try {
			HttpClient client = HttpClient.newHttpClient();
			String json = "{\"content\": " + objectMapper.writeValueAsString(content) + "}";
			log.info("Sent message to Discord" + json);
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(this.discordWebhookUrl))
					.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
			client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (Exception e) {
			log.error("Failed to send message to Discord", e);
		}
	}

}
