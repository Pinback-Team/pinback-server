package com.pinback.application.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "job-images")
public class JobImageConfig {
	private Map<String, String> images;

	public String getImageUrl(String key) {
		if (images == null)
			return null;
		return images.get(key.toLowerCase());
	}

	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}
}
