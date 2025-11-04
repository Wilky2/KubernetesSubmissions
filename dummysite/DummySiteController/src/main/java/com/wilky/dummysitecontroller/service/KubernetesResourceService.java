package com.wilky.dummysitecontroller.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ConfigMapBuilder;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1.IngressBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

@org.springframework.stereotype.Service
public class KubernetesResourceService {

	private final KubernetesClient client;

	private static Logger LOG = LoggerFactory.getLogger(KubernetesResourceService.class);

	public KubernetesResourceService(KubernetesClient client) {
		this.client = client;
	}

	public void createResources(String namespace, String name, String websiteUrl) {
		try {
			// 1️⃣ Download the HTML content
			String html = downloadWebsite(websiteUrl);
			if (html == null || html.isEmpty()) {
				LOG.error("⚠️ Failed to download website content from: " + websiteUrl);
				return;
			}

			// 2️⃣ Create a ConfigMap with the HTML content
			String configMapName = name + "-html";
			ConfigMap htmlConfigMap = new ConfigMapBuilder().withNewMetadata().withName(configMapName)
					.withNamespace(namespace).endMetadata().withData(Map.of("index.html", html)).build();

			client.configMaps().inNamespace(namespace).resource(htmlConfigMap).createOrReplace();

			// 3️⃣ Create Nginx Deployment that serves the ConfigMap content
			Deployment deployment = new DeploymentBuilder().withNewMetadata().withName(name + "-dep")
					.withNamespace(namespace).endMetadata().withNewSpec().withReplicas(1).withNewSelector()
					.addToMatchLabels("app", name).endSelector().withNewTemplate().withNewMetadata()
					.addToLabels("app", name).endMetadata().withNewSpec().addNewContainer().withName("nginx")
					.withImage("nginx:alpine").addNewPort().withContainerPort(80).endPort().addNewVolumeMount()
					.withName("html-volume").withMountPath("/usr/share/nginx/html").endVolumeMount().endContainer()
					.addNewVolume().withName("html-volume").withNewConfigMap().withName(configMapName).endConfigMap()
					.endVolume().endSpec().endTemplate().endSpec().build();

			client.apps().deployments().inNamespace(namespace).resource(deployment).createOrReplace();

			// 4️⃣ Create a Service
			Service service = new ServiceBuilder().withNewMetadata().withName(name + "-svc").withNamespace(namespace)
					.endMetadata().withNewSpec().addToSelector("app", name).addNewPort().withPort(80)
					.withTargetPort(new IntOrString(80)).endPort().endSpec().build();

			// 5️⃣ Create an Ingress
			Ingress ingress = new IngressBuilder().withNewMetadata().withName(name + "-ing").withNamespace(namespace)
					.endMetadata().withNewSpec().addNewRule().withHost(name + ".example.com").withNewHttp().addNewPath()
					.withPath("/").withPathType("Prefix").withNewBackend().withNewService().withName(name + "-svc")
					.withNewPort().withNumber(80).endPort().endService().endBackend().endPath().endHttp().endRule()
					.endSpec().build();

			client.network().v1().ingresses().inNamespace(namespace).resource(ingress).createOrReplace();

			client.services().inNamespace(namespace).resource(service).createOrReplace();

			LOG.info("✅ DummySite deployed successfully: " + name + " (" + websiteUrl + ")");

		} catch (Exception e) {
			LOG.error("❌ Error creating resources for DummySite " + name + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Downloads the main HTML content of a website.
	 */
	private String downloadWebsite(String url) throws IOException, InterruptedException {
		LOG.info("🌐 Downloading content from: " + url);

		HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET()
				.header("User-Agent", "DummySiteOperator/1.0").build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() >= 200 && response.statusCode() < 300) {
			System.out.println("✅ Downloaded website content (" + response.body().length() + " bytes)");
			return response.body();
		}

		LOG.error("⚠️ Failed to fetch: " + url + " (status " + response.statusCode() + ")");
		return null;
	}

}
