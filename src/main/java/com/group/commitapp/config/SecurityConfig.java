package com.group.commitapp.config;

import com.group.commitapp.config.jwt.JwtAccessDeniedHandler;
import com.group.commitapp.config.jwt.JwtAuthenticationEntryPoint;
import com.group.commitapp.config.jwt.filter.JwtAuthenticationFilter;
import com.group.commitapp.config.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtUtil jwtUtil;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
				// CSRF
				.csrf(AbstractHttpConfigurer::disable)
				// 토큰 방식을 위한 STATELESS 선언
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// 권한 규칙 설정 (API 명세에 맞게 수정 필요)
				.authorizeHttpRequests(
						(requests) ->
								requests
										.requestMatchers("/api/v1/user/**", "*")
										.hasRole("USER")
										.anyRequest()
										.permitAll())
				// 커스텀 JWT 핸들러 및 엔트리 포인트를 사용하기 위해 httpBasic disable
				.httpBasic(AbstractHttpConfigurer::disable)
				// 인증 및 인가에 대한 예외 처리를 다룸
				.exceptionHandling(
						(httpSecurityExceptionHandlingConfigurer) ->
								httpSecurityExceptionHandlingConfigurer
										// 인증 실패
										.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
										// 인가 실패
										.accessDeniedHandler(new JwtAccessDeniedHandler()))
				// JWT Filter 를 필터체인에 끼워넣어줌
				.addFilterBefore(
						new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors()
				.and()
				.csrf()
				.disable()
				.authorizeHttpRequests((authz) -> authz.anyRequest().authenticated());
		return http.build();
	}
}
