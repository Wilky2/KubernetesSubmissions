package com.wilky.dummysitecontroller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DummySiteSpec {
	@JsonProperty("website_url")
	private String websiteUrl;
}