package com.pinback.application.article.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.domain.article.entity.Article;
import com.pinback.domain.category.entity.Category;
import com.pinback.domain.user.entity.User;

public interface ArticleGetServicePort {
	Optional<Article> findRecentByUser(User user);

	boolean checkExistsByUserAndUrl(User user, String url);

	Article findById(long articleId);

	Optional<Article> findByUrlAndUser(User user, String url);

	Article findByUserAndId(User user, long articleId);

	ArticlesWithUnreadCountDto findAll(User user, PageRequest pageRequest);

	ArticlesWithUnreadCountDto findAllByCategory(User user, Category category, boolean isRead, PageRequest pageRequest);

	ArticlesWithUnreadCountDto findUnreadArticles(User user, PageRequest pageRequest);

	Page<Article> findTodayRemind(User user, LocalDateTime remindDateTime, Pageable pageable, Boolean isRead);

	RemindArticlesWithCountDto findTodayRemindWithCount(User user, LocalDateTime remindDateTime, Pageable pageable,
		Boolean isRead);
}
