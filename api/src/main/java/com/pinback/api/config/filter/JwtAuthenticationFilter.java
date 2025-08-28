package com.pinback.api.config.filter;

import static com.pinback.shared.constant.ExceptionCode.*;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pinback.infrastructure.jwt.JwtUtil;
import com.pinback.infrastructure.jwt.exception.ExpiredTokenException;
import com.pinback.infrastructure.jwt.exception.InvalidTokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String token = request.getHeader(AUTHORIZATION_HEADER);

		if (!StringUtils.hasText(token)) {
			handleTokenException(response, EMPTY_TOKEN.getMessage(), 401, EMPTY_TOKEN.getCode());
			return;
		}

		String extractToken = jwtUtil.extractToken(token);

		try {
			UUID userId = jwtUtil.extractId(extractToken);

			Authentication authentication = new UsernamePasswordAuthenticationToken(
				userId, null, null);

			SecurityContextHolder.getContext().setAuthentication(authentication);

		} catch (ExpiredTokenException e) {
			log.warn("만료된 토큰: {}", token);
			handleTokenException(response, EXPIRED_TOKEN.getMessage(), 401, EXPIRED_TOKEN.getCode());
			return;

		} catch (InvalidTokenException e) {
			log.warn("유효하지 않은 토큰: {}", token);
			handleTokenException(response, INVALID_TOKEN.getMessage(), 401, INVALID_TOKEN.getCode());
			return;

		} catch (Exception e) {
			log.error("토큰 처리 중 오류 발생", e);
			handleTokenException(response, INTERNAL_SERVER_ERROR.getMessage(), 500, INTERNAL_SERVER_ERROR.getCode());
			return;
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * 토큰 예외 처리
	 */
	private void handleTokenException(HttpServletResponse response, String message, int status, String code)
		throws IOException {
		response.setStatus(status);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(
			String.format("{\"code\": \"%s\", \"message\": \"%s\"}", code, message)
		);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();

		return
			path.startsWith("/api/v1/auth/signup") ||
				path.startsWith("/api/v1/auth/token") ||
				path.startsWith("/api/public/") ||
				path.equals("/health") ||
				path.startsWith("/swagger") ||
				path.startsWith("/v3/api-docs") ||
				path.startsWith("/docs") ||
				path.startsWith("/api/v1/test/push");
	}
}
