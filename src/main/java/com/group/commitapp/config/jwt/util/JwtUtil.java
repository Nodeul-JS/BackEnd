package com.group.commitapp.config.jwt.util;

import com.group.commitapp.config.security.PrincipalDetailsService;
import com.group.commitapp.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Getter
@Slf4j
public class JwtUtil {
	private final UserRepository userRepository;
	private final PrincipalDetailsService userDetailsService;
	private final String SECRET_KEY;

	public static final long TOKEN_VALID_TIME = 1000L * 60 * 5 * 5; // 5분

	public JwtUtil(
			@Value("${jwt.secret}") String secretKey,
			UserRepository userRepository,
			PrincipalDetailsService userDetailsService) {
		this.SECRET_KEY = secretKey;
		this.userRepository = userRepository;
		this.userDetailsService = userDetailsService;
	}

	public Key getSigningKey(String secretKey) {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public Claims extractAllClaims(String jwtToken) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey(SECRET_KEY))
				.build()
				.parseClaimsJws(jwtToken)
				.getBody();
	}

	public String getIdInToken(String token) {
		return extractAllClaims(token).get("id", String.class);
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getIdInToken(token));
		log.info("getAuthorities() : " + userDetails.getAuthorities().toString());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String createToken(String memberId) {
		Claims claims = Jwts.claims().setSubject(memberId);
		claims.put("id", memberId);
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
				.signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
				.compact();
	}

	public String resolveToken(String token) {
		if (token != null) {
			return token.substring("Bearer ".length());
		} else {
			return "";
		}
	}

	public Claims parseToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey(SECRET_KEY))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public Long getExpiration(String jwtToken) {
		Date expiration =
				Jwts.parserBuilder()
						.setSigningKey(getSigningKey(SECRET_KEY))
						.build()
						.parseClaimsJws(jwtToken)
						.getBody()
						.getExpiration();
		long now = System.currentTimeMillis();
		return expiration.getTime() - now;
	}
}
