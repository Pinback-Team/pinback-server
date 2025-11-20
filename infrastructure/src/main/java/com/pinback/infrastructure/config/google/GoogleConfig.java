package com.pinback.infrastructure.config.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GoogleConfig {

	@Value("${google.client-id}")
	private String clientId;

	@Value("${google.client-secret}")
	private String clientSecret;

	@Value("${google.redirect-uri}")
	private String redirectUri;

	@Value("${google.token-uri}")
	private String tokenUri;

	@Value("${google.user-info-uri}")
	private String userInfoUri;

	@Bean
	public WebClient googleWebClient() {
		return WebClient.builder().build();
	}

	@Bean
	public String googleClientId() {
		return clientId;
	}

	@Bean
	public String googleClientSecret() {
		return clientSecret;
	}

	@Bean
	public String googleRedirectUri() {
		return redirectUri;
	}

	@Bean
	public String googleTokenUri() {
		return tokenUri;
	}

	@Bean
	public String googleUserInfoUri() {
		return userInfoUri;
	}
}
