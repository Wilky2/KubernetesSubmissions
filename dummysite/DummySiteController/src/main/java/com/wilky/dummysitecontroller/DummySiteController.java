package com.wilky.dummysitecontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.wilky.dummysitecontroller.model.DummySite;
import com.wilky.dummysitecontroller.service.KubernetesResourceService;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;

@Component
public class DummySiteController implements CommandLineRunner {

	private static final Logger LOG = LoggerFactory.getLogger(DummySiteController.class);

	private final KubernetesClient client;
	private final KubernetesResourceService resourceService;

	public DummySiteController(KubernetesClient client, KubernetesResourceService resourceService) {
		this.client = client;
		this.resourceService = resourceService;
	}

	public void run(String... args) {
		LOG.info("👀 Starting DummySite watcher...");

		// Create informer
		SharedIndexInformer<DummySite> informer = client.resources(DummySite.class).inAnyNamespace()
				.runnableInformer(0);

		informer.addEventHandler(new ResourceEventHandler<>() {
			@Override
			public void onAdd(DummySite dummySite) {
				String namespace = dummySite.getMetadata().getNamespace();
				String name = dummySite.getMetadata().getName();
				String websiteUrl = dummySite.getSpec().getWebsiteUrl();
				LOG.info("🆕 DummySite created: {} in namespace {}", name, namespace);
				try {
					resourceService.createResources(namespace, name, websiteUrl);
				} catch (Exception e) {
					LOG.error("❌ Error while creating resources for DummySite {}", name, e);
				}
			}

			@Override
			public void onUpdate(DummySite oldObj, DummySite newObj) {
				LOG.info("🔁 DummySite updated: {}", newObj.getMetadata().getName());
			}

			@Override
			public void onDelete(DummySite obj, boolean deletedFinalStateUnknown) {
				LOG.info("❌ DummySite deleted: {}", obj.getMetadata().getName());
			}
		});

		// ✅ Run informer in a background thread so the app stays alive
		Thread informerThread = new Thread(informer::run, "dummy-site-informer");
		informerThread.setDaemon(false);
		informerThread.start();

		// Keep process alive
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			LOG.error("❌ Error while keeping alive", e);
		}

		LOG.info("✅ DummySite watcher started successfully and running in background.");
	}
}
