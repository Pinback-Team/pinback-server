package com.pinback.application.test.port.out;

import com.pinback.application.article.dto.response.ArticleMetadataResponse;

public interface ArticleMetadataPort {
	ArticleMetadataResponse extractMetadata(String url);
}
