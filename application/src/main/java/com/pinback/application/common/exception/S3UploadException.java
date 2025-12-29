package com.pinback.application.common.exception;

import com.pinback.shared.constant.ExceptionCode;
import com.pinback.shared.exception.ApplicationException;

public class S3UploadException extends ApplicationException {
	public S3UploadException() {

		super(ExceptionCode.S3_UPLOAD_ERROR);
	}
}
