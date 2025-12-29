package com.pinback.application.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "profile-images")
public class ProfileImageConfig {
	private Map<String, String> images;

	public String getImageUrl(String key) {
		return images.get(key);
	}

	public Map<String, String> getImages() {
		return images;
	}

	public void setImages(Map<String, String> images) {
		this.images = images;
	}

	public boolean isValidImageKey(String imageKey) {
		return images != null && images.containsKey(imageKey);
	}
}
