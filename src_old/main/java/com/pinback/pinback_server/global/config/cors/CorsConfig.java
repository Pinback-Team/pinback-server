package com.pinback.pinback_server.global.config.cors;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOriginPatterns(List.of(
			"http://localhost:3000",
			"http://localhost:3001",
			"chrome-extension://*",
			"http://localhost:5173",
			"http://localhost:5174",
			"https://pinback-client.vercel.app",
			"https://pinback.today",
			"https://landing.pinback.today",
			"https://www.pinback.today"
		));

		configuration.setAllowedMethods(Arrays.asList(
			"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
		));

		configuration.setAllowedHeaders(Arrays.asList(
			"Authorization",
			"Content-Type",
			"X-Requested-With",
			"Accept",
			"Origin",
			"Access-Control-Request-Method",
			"Access-Control-Request-Headers",
			"X-Forwarded-For",
			"X-Real-IP",
			""
		));

		configuration.setAllowCredentials(false);

		configuration.setExposedHeaders(Arrays.asList(
			"Access-Control-Allow-Origin",
			"Access-Control-Allow-Credentials",
			"Authorization",
			"Content-Disposition"
		));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
