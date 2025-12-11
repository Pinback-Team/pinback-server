package com.pinback.application.user.usecase;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pinback.application.common.exception.GoogleProfileImageMissingException;
import com.pinback.application.common.exception.S3UploadException;
import com.pinback.application.s3.port.out.S3StorageServicePort;
import com.pinback.shared.exception.ApplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOAuthUsecase {
	private static final String DEFAULT_CONTENT_TYPE = "image/jpeg";
	private final S3StorageServicePort s3StorageService;
	private final WebClient webClient;

	/**
	 * Google URL에서 이미지를 다운로드하여 S3에 업로드하고, S3 URL을 Mono로 반환
	 */
	public Mono<String> saveProfileImage(UUID userId, String imageUrl) {

		if (imageUrl == null || imageUrl.isBlank()) {
			log.warn("Google 이미지 URL 누락. 사용자 생성 취소.");
			throw new GoogleProfileImageMissingException();
		}

		// 1. WebClient를 사용하여 이미지 데이터를 byte[]로 다운로드
		return webClient.get()
			.uri(imageUrl)
			.retrieve()
			.toEntity(byte[].class)
			.flatMap(responseEntity -> {

				byte[] imageBytes = responseEntity.getBody();
				if (imageBytes == null || imageBytes.length == 0) {
					log.warn("이미지 다운로드 실패: 유효한 데이터 획득 실패.");
					throw new GoogleProfileImageMissingException();
				}

				// Content-Type 헤더 추출
				String contentType = responseEntity.getHeaders().getContentType() != null
					? responseEntity.getHeaders().getContentType().toString()
					: DEFAULT_CONTENT_TYPE;

				// 2. byte[]를 InputStream으로 변환 및 S3 업로드
				try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes)) {

					String s3Url = s3StorageService.uploadProfileImage(
						inputStream,
						imageBytes.length,
						contentType,
						userId
					);
					return Mono.just(s3Url);

				} catch (ApplicationException e) {
					throw e;
				} catch (Exception e) {
					log.error("InputStream 처리 또는 S3 업로드 중 알 수 없는 오류 발생. User ID: {}", userId, e);
					throw new S3UploadException();
				}
			})
			.onErrorResume(e -> {
				// WebClient 다운로드 실패(네트워크 오류, 404 등) 시 처리
				log.error("Google 이미지 다운로드 중 WebClient 오류 발생. User ID: {}", userId, e);
				return Mono.error(new S3UploadException());
			});
	}

}
