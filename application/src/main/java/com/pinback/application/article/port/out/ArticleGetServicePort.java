package com.pinback.application.article.port.out;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.pinback.application.article.dto.ArticleCountInfoDtoV3;
import com.pinback.application.article.dto.ArticlesWithCountDto;
import com.pinback.application.article.dto.ArticlesWithUnreadCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDto;
import com.pinback.application.article.dto.RemindArticlesWithCountDtoV2;
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

	ArticlesWithUnreadCountDto findAllByCategory(User user, Category category, Boolean isRead, PageRequest pageRequest);

	ArticlesWithUnreadCountDto findUnreadArticles(User user, PageRequest pageRequest);

	Page<Article> findTodayRemind(User user, LocalDateTime remindDateTime, Pageable pageable, Boolean isRead);

	RemindArticlesWithCountDto findTodayRemindWithCount(User user, LocalDateTime startDateTime,
		LocalDateTime endDateTime, Pageable pageable,
		Boolean isRead);

	RemindArticlesWithCountDtoV2 findTodayRemindWithCountV2(User user, LocalDateTime startDateTime,
		LocalDateTime endDateTime, Pageable pageable, Boolean isReadAfterRemind);

	ArticleCountInfoDtoV3 findTodayRemindCountV3(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);

	ArticlesWithCountDto findAllByReadStatus(User user, Boolean readStatus, PageRequest pageRequest);

	ArticleCountInfoDtoV3 findAllCountV3(User user);
}
