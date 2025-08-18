package com.pinback.domain.article.service;

import com.pinback.domain.article.entity.Article;
import com.pinback.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleSaveService {
    
    private final ArticleRepository articleRepository;

    public Article save(Article article) {
        return articleRepository.save(article);
    }
}