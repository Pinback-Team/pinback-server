package com.pinback.infrastructure.s3.service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pinback.application.common.exception.S3UploadException;
import com.pinback.application.s3.port.out.S3StorageServicePort;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@Service
public class S3StorageService implements S3StorageServicePort {
	private static final String GOOGLE_PROFILE_IMAGE_FOLDER = "google_profile_image/";
	private static final String ARTICLE_THUMBNAIL_FOLDER = "articles_thumbnails/";
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
		UUID uuid = UUID.randomUUID();
		String keyName = GOOGLE_PROFILE_IMAGE_FOLDER + userId + "_" + uuid + ".jpg";
		return executeUpload(inputStream, fileSize, contentType, keyName);
	}

	/**
	 * URL 메타데이터 썸네일 업로드
	 */
	public String uploadArticleThumbnail(InputStream inputStream, long fileSize, String contentType,
		String originalImageUrl) {
		String hashedName = hashUrl(originalImageUrl);
		String keyName = ARTICLE_THUMBNAIL_FOLDER + hashedName + ".jpg";

		// 1. 이미 존재하는지 확인 (중복 업로드 방지)
		if (isExists(keyName)) {
			log.info("이미 존재하는 썸네일입니다. 기존 URL 반환: {}", keyName);
			return generatePublicUrl(keyName);
		}

		// 2. 존재하지 않으면 업로드 실행
		return executeUpload(inputStream, fileSize, contentType, keyName);
	}

	/**
	 * 실제 S3 PutObject 실행 공통 로직
	 */
	private String executeUpload(InputStream inputStream, long fileSize, String contentType, String keyName) {
		try {
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(keyName)
				.contentLength(fileSize)
				.contentType(contentType)
				.build();

			PutObjectResponse response = s3Client.putObject(
				putObjectRequest,
				RequestBody.fromInputStream(inputStream, fileSize)
			);

			if (response.sdkHttpResponse().isSuccessful()) {
				String s3PublicUrl = generatePublicUrl(keyName);
				log.info("S3 업로드 성공. URL: {}", s3PublicUrl);
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

	/**
	 * S3 공개 URL 생성
	 */
	private String generatePublicUrl(String keyName) {
		return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, regionName, keyName);
	}

	/**
	 * S3에 객체가 존재하는지 확인하는 메서드
	 */
	private boolean isExists(String keyName) {
		try {
			s3Client.headObject(HeadObjectRequest.builder()
				.bucket(bucketName)
				.key(keyName)
				.build());
			return true;
		} catch (S3Exception e) {
			if (e.statusCode() == 404) {
				return false;
			}
			log.error("S3 API 오류 발생: {}", e.getMessage());
			throw new S3UploadException();
		}
	}

	/**
	 * URL을 안전한 문자열로 바꾸는 메서드
	 */
	private String hashUrl(String url) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(url.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hash)
				sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// MD5는 표준이라 거의 발생하지 않지만, 예외 시 UUID로 대체하는 방어 로직
			return UUID.randomUUID().toString();
		}
	}

}
