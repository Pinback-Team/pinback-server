package com.pinback.application.article.port.out;

import com.pinback.domain.article.entity.Article;

public interface ArticleSaveServicePort {
	Article save(Article article);
}
