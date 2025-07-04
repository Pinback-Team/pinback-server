package com.pinback.pinback_server.global.config.auth.filter;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pinback.pinback_server.global.common.jwt.JwtUtil;
import com.pinback.pinback_server.global.common.jwt.exception.ExpiredTokenException;
import com.pinback.pinback_server.global.common.jwt.exception.InvalidTokenException;

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

		String token = jwtUtil.extractToken(request.getHeader(AUTHORIZATION_HEADER));

		if (StringUtils.hasText(token)) {
			try {
				UUID userId = jwtUtil.extractId(token);

				Authentication authentication = new UsernamePasswordAuthenticationToken(
					userId, null, null);

				SecurityContextHolder.getContext().setAuthentication(authentication);

				log.debug("JWT 인증 성공: userId = {}", userId);

			} catch (ExpiredTokenException e) {
				log.warn("만료된 토큰: {}", token);
				handleTokenException(response, "만료된 토큰입니다.", 401);
				return;

			} catch (InvalidTokenException e) {
				log.warn("유효하지 않은 토큰: {}", token);
				handleTokenException(response, "유효하지 않은 토큰입니다.", 401);
				return;

			} catch (Exception e) {
				log.error("토큰 처리 중 오류 발생", e);
				handleTokenException(response, "토큰 처리 중 오류가 발생했습니다.", 500);
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * 토큰 예외 처리
	 */
	private void handleTokenException(HttpServletResponse response, String message, int status)
		throws IOException {
		response.setStatus(status);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(
			String.format("{\"error\": \"%s\", \"status\": %d}", message, status)
		);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();

		return path.startsWith("/api/v1/auth/signup") ||
			path.startsWith("/api/public/") ||
			path.equals("/health") ||
			path.startsWith("/swagger") ||
			path.startsWith("/v3/api-docs") ||
			path.startsWith("/docs");
	}
}