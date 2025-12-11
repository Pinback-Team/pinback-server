package com.pinback.application.s3.port.out;

import java.io.InputStream;
import java.util.UUID;

public interface S3StorageServicePort {
	String uploadProfileImage(InputStream inputStream, long fileSize, String contentType, UUID userId);
}
