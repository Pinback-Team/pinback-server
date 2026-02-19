package com.pinback.application.google.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pinback.application.common.exception.GoogleApiException;
import com.pinback.application.common.exception.GoogleEmailMissingException;
import com.pinback.application.common.exception.GoogleNameMissingException;
import com.pinback.application.common.exception.GoogleProfileImageMissingException;
import com.pinback.application.common.exception.GoogleTokenMissingException;
import com.pinback.application.common.exception.InvalidGoogleUriException;
import com.pinback.application.google.dto.response.GoogleApiResponse;
import com.pinback.application.google.dto.response.GoogleTokenResponse;
import com.pinback.application.google.dto.response.GoogleUserInfoResponse;
import com.pinback.application.google.port.out.GoogleOAuthPort;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GoogleOAuthClient implements GoogleOAuthPort {
	private final WebClient googleWebClient;
	private final String googleClientId;
	private final String googleClientSecret;
	private final List<String> googleRedirectUris;
	private final String googleTokenUri;
	private final String googleUserInfoUri;

	public GoogleOAuthClient(
		WebClient googleWebClient,
		@Qualifier("googleClientId") String googleClientId,
		@Qualifier("googleClientSecret") String googleClientSecret,
		@Qualifier("googleRedirectUris") List<String> googleRedirectUris,
		@Qualifier("googleTokenUri") String googleTokenUri,
		@Qualifier("googleUserInfoUri") String googleUserInfoUri) {
		this.googleWebClient = googleWebClient;
		this.googleClientId = googleClientId;
		this.googleClientSecret = googleClientSecret;
		this.googleRedirectUris = googleRedirectUris;
		this.googleTokenUri = googleTokenUri;
		this.googleUserInfoUri = googleUserInfoUri;
	}

	@Override
	public Mono<GoogleUserInfoResponse> fetchUserInfo(String code) {

		return requestAccessToken(code)
			// 토큰 응답을 UserInfo 요청으로 변환하여 연결
			.flatMap(tokenResponse -> {

				// Access Token 유효성 검증
				if (tokenResponse == null || tokenResponse.accessToken() == null) {
					log.info("tokenResponse: {}", tokenResponse);
					log.error("Google Access Token 획득 실패: 응답 본문에 토큰이 없습니다. Code: {}", code);
					return Mono.error(new GoogleTokenMissingException());
				}
				// Access Token으로 사용자 정보 요청
				return getUserInfo(tokenResponse.accessToken());
			});
	}

	@Override
	public Mono<GoogleUserInfoResponse> fetchUserInfoV3(String code, String uri) {

		return requestAccessTokenV3(code, uri)
			// 토큰 응답을 UserInfo 요청으로 변환하여 연결
			.flatMap(tokenResponse -> {

				// Access Token 유효성 검증
				if (tokenResponse == null || tokenResponse.accessToken() == null) {
					log.info("tokenResponse: {}", tokenResponse);
					log.error("Google Access Token 획득 실패: 응답 본문에 토큰이 없습니다. Code: {}", code);
					return Mono.error(new GoogleTokenMissingException());
				}
				// Access Token으로 사용자 정보 요청
				return getUserInfo(tokenResponse.accessToken());
			});
	}

	private Mono<GoogleTokenResponse> requestAccessToken(String code) {
		String firstRedirectUri = googleRedirectUris.getFirst();
		log.info("redirect: {}", firstRedirectUri);
		String requestBody = "code=" + code +
			"&client_id=" + googleClientId +
			"&client_secret=" + googleClientSecret +
			"&redirect_uri=" + firstRedirectUri +
			"&grant_type=authorization_code";

		return googleWebClient.post()
			.uri(googleTokenUri)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(requestBody)
			.retrieve()
			// HTTP 오류 발생 시
			.onStatus(status -> status.isError(), clientResponse ->
				clientResponse.bodyToMono(String.class)
					.flatMap(body -> {
						String errorLog = String.format(
							"[GoogleOAuth API 에러] HTTP Status: %s, Detail: %s",
							clientResponse.statusCode(), body
						);
						log.error(errorLog);
						return Mono.error(new GoogleApiException());
					})
			)
			.bodyToMono(GoogleTokenResponse.class);
	}

	private Mono<GoogleTokenResponse> requestAccessTokenV3(String code, String uri) {
		if (!googleRedirectUris.contains(uri)) {
			log.error("허용되지 않은 Redirect URI 요청: {}", uri);

			return Mono.error(new InvalidGoogleUriException());
		}

		log.info("redirect: {}", uri);
		String requestBody = "code=" + code +
			"&client_id=" + googleClientId +
			"&client_secret=" + googleClientSecret +
			"&redirect_uri=" + uri +
			"&grant_type=authorization_code";

		return googleWebClient.post()
			.uri(googleTokenUri)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.bodyValue(requestBody)
			.retrieve()
			// HTTP 오류 발생 시
			.onStatus(status -> status.isError(), clientResponse ->
				clientResponse.bodyToMono(String.class)
					.flatMap(body -> {
						String errorLog = String.format(
							"[GoogleOAuth API 에러] HTTP Status: %s, Detail: %s",
							clientResponse.statusCode(), body
						);
						log.error(errorLog);
						return Mono.error(new GoogleApiException());
					})
			)
			.bodyToMono(GoogleTokenResponse.class);
	}

	private Mono<GoogleUserInfoResponse> getUserInfo(String accessToken) {
		return googleWebClient.get()
			.uri(googleUserInfoUri)
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.onStatus(status -> status.isError(), clientResponse ->
				clientResponse.bodyToMono(String.class)
					.flatMap(body -> {
						String errorLog = String.format(
							"[Google UserInfo API 에러] HTTP Status: %s, Detail: %s",
							clientResponse.statusCode(), body
						);
						log.error(errorLog);
						return Mono.error(new GoogleApiException());
					})
			)
			.bodyToMono(GoogleApiResponse.class)
			.map(apiData -> {
				log.info("Google UserInfo API 전체 응답 데이터: {}", apiData);
				// 이메일 필드 유효성 검사
				if (apiData.email() == null || apiData.email().isBlank()) {
					log.error("Google UserInfo 응답에 이메일 필드가 누락되었습니다.");
					throw new GoogleEmailMissingException();
				}

				// 프로필 필드 유효성 검사
				if (apiData.picture() == null || apiData.picture().isBlank()) {
					log.error("Google UserInfo 응답에 프로필 필드가 누락되었습니다.");
					throw new GoogleProfileImageMissingException();
				}

				// 이름 필드 유효성 검사
				String securedName = getSecuredName(apiData);
				if (securedName.isBlank()) {
					log.error("Google UserInfo 응답에 이름 필드가 누락되었습니다.");
					throw new GoogleNameMissingException();
				}

				// 최종 DTO로 변환하여 반환
				return GoogleUserInfoResponse.from(apiData.email(), apiData.picture(), securedName);
			});
	}

	private String getSecuredName(GoogleApiResponse apiData) {
		String givenName = apiData.givenName();
		String familyName = apiData.familyName();
		String fullName = apiData.name();

		// given + family name
		if (givenName != null && !givenName.isBlank() && familyName != null && !familyName.isBlank()) {
			return givenName + " " + familyName;
		}

		// fullName
		if (fullName != null && !fullName.isBlank()) {
			return fullName;
		}

		// 모든 필드 존재 x
		return "";
	}

}
