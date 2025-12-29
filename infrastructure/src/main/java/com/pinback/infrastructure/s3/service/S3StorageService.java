package com.pinback.infrastructure.s3.service;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pinback.application.common.exception.S3UploadException;
import com.pinback.application.s3.port.out.S3StorageServicePort;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
public class S3StorageService implements S3StorageServicePort {
	private static final String GOOGLE_PROFILE_IMAGE_FOLDER = "google_profile_image/";
	private final S3Client s3Client;
	private final String bucketName;
	private final String regionName;

	public S3StorageService(
		S3Client s3Client,
		@Value("${cloud.aws.s3.bucket}")
		String bucketName,
		@Value("${cloud.aws.region.static}")
		String regionName
	) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
		this.regionName = regionName;
	}

	/**
	 * InputStream 형태의 프로필 이미지 데이터를 S3에 업로드하고 객체 URL 반환
	 */
	public String uploadProfileImage(InputStream inputStream, long fileSize, String contentType, UUID userId) {

		// 1. 객체 키(Key Name) 생성: image/{userId}_{UUID}.jpg
		UUID uuid = UUID.randomUUID();
		String keyName = GOOGLE_PROFILE_IMAGE_FOLDER + userId + "_" + uuid + ".jpg";

		try {
			// 2. S3 PutObject 요청 구성
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(keyName)
				.contentLength(fileSize)
				.contentType(contentType)
				.build();

			// 3. S3에 업로드 실행
			PutObjectResponse response = s3Client.putObject(
				putObjectRequest,
				RequestBody.fromInputStream(inputStream, fileSize)
			);

			// 4. 업로드 결과 확인 및 URL 반환
			if (response.sdkHttpResponse().isSuccessful()) {
				// S3 공개 URL 형식: https://{bucketName}.s3.{region}.amazonaws.com/{keyName}
				String s3PublicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, regionName,
					keyName);
				log.info("S3 프로필 이미지 업로드 성공. URL: {}", s3PublicUrl);
				return s3PublicUrl;
			} else {
				log.error("S3 PutObject 요청 성공이나, 응답 상태가 성공이 아님: {}",
					response.sdkHttpResponse().statusText().orElse("Unknown Status"));
				throw new S3UploadException();
			}
		} catch (S3Exception e) {
			log.error("S3 API 오류 발생: {}", e.getMessage());
			throw new S3UploadException();
		} catch (Exception e) {
			log.error("S3 업로드 중 일반 오류 발생: {}", e.getMessage());
			throw new S3UploadException();
		}
	}
}
