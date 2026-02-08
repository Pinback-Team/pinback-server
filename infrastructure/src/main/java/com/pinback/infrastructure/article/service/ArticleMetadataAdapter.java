package com.pinback.infrastructure.article.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pinback.application.article.dto.response.ArticleMetadataResponse;
import com.pinback.application.common.exception.S3UploadException;
import com.pinback.application.test.port.out.ArticleMetadataPort;
import com.pinback.domain.article.exception.ArticleTitleNotFoundException;
import com.pinback.domain.article.exception.InvalidUrlException;
import com.pinback.infrastructure.s3.service.S3StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleMetadataAdapter implements ArticleMetadataPort {
	private static final String COMMON_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
	private static final int TIMEOUT_MILLIS = 8000;
	private final S3StorageService s3StorageService;
	@Value("${default-thumbnail}")
	private String DEFAULT_THUMBNAIL_URL;

	@Override
	public ArticleMetadataResponse extractMetadata(String url) {
		String processedUrl = preProcessUrl(url);
		try {
			// 1. 웹페이지 접속
			Document doc = Jsoup.connect(processedUrl)
				.userAgent(COMMON_USER_AGENT)
				.referrer("https://www.google.com/")
				.header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8")
				.timeout(TIMEOUT_MILLIS)
				.get();

			// 2. 제목 추출 (Open Graph -> HTML Title 순)
			String title = extractMetaContent(doc, "meta[property=og:title]", false);
			if (title.isBlank()) {
				title = doc.title();
			}

			// 제목을 불러올 수 없는 경우 예외 처리
			if (title.isBlank()) {
				log.error("제목 추출 실패 (URL: {})", url);
				throw new ArticleTitleNotFoundException();
			}

			// 3. 썸네일 추출 (Open Graph)
			String originalThumbnail = extractMetaContent(doc, "meta[property=og:image]", true);

			// 썸네일이 없는 경우 기본 이미지로 처리
			String finalThumbnail;
			if (originalThumbnail.isBlank()) {
				finalThumbnail = DEFAULT_THUMBNAIL_URL;
			} else {
				finalThumbnail = uploadToS3(originalThumbnail);
			}

			return ArticleMetadataResponse.of(title, finalThumbnail);

		} catch (IOException | IllegalArgumentException e) {
			log.error("URL 정보를 가져오는 중 오류 발생: {} / 사유: {}", url, e.getMessage());
			throw new InvalidUrlException();
		}

	}

	private String extractMetaContent(Document doc, String selector, boolean isUrl) {
		if (isUrl) {
			String absContent = doc.select(selector).attr("abs:content").trim();
			return absContent.isEmpty() ? doc.select(selector).attr("content").trim() : absContent;
		}
		return doc.select(selector).attr("content").trim();
	}

	private String preProcessUrl(String url) {
		if (url == null || url.isBlank()) {
			return url;
		}

		// 1. 네이버 블로그 처리: blog.naver.com -> m.blog.naver.com
		if (url.contains("blog.naver.com") && !url.contains("m.blog.naver.com")) {
			return url.replace("blog.naver.com", "m.blog.naver.com");
		}

		// 2. 티스토리 처리: tistory.com 게시글 -> 도메인 뒤에 /m 붙이기 (메인 페이지 제외)
		if (url.contains("tistory.com") && !url.contains("tistory.com/m/") && isTistoryPost(url)) {
			return url.replaceFirst("tistory.com/", "tistory.com/m/");
		}

		return url;
	}

	private boolean isTistoryPost(String url) {
		// 도메인 뒤에 추가 경로(숫자나 문자)가 있으면 포스팅으로 간주
		String path = url.replaceFirst("https?://[^/]+", "");
		return path.length() > 1;
	}

	private String uploadToS3(String originalThumbnailUrl) {
		try {
			URL url = new URL(originalThumbnailUrl);
			URLConnection conn = url.openConnection();

			// 타임아웃 설정
			conn.setConnectTimeout(3000);
			conn.setReadTimeout(3000);

			try (InputStream is = conn.getInputStream()) {
				long fileSize = conn.getContentLengthLong();
				String contentType = conn.getContentType();

				return s3StorageService.uploadArticleThumbnail(is, fileSize, contentType, originalThumbnailUrl);
			}
		} catch (Exception e) {
			log.warn("썸네일 S3 업로드 실패, 원본 URL을 사용합니다. 사유: {}", e.getMessage());
			throw new S3UploadException();
		}
	}
}
