package com.pinback.infrastructure.config.firebase;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

	@Value("${fcm}")
	private String fcm;

	@PostConstruct
	public void init() {
		try {
			InputStream serviceAccount = new FileInputStream(fcm);

			FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
			serviceAccount.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
