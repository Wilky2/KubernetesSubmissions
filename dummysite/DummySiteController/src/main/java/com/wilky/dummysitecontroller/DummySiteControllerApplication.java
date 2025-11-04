package com.wilky.dummysitecontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@SpringBootApplication
public class DummySiteControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DummySiteControllerApplication.class, args);
	}

	@Bean
	public KubernetesClient kubernetesClient() {
		return new DefaultKubernetesClient();
	}

}
