package com.example.demo.support.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenResolver {

	private static final String MEMBER_ID_CLAIM_KEY = "memberId";
	private static final String MEMBER_ROLE_CLAIM_KEY = "memberRole";

	@Value("${security.jwt.token.secretkey}")
	private String secretKey;

	/**
	 * 토큰의 클레임 정보를 추출 합니다.
	 *
	 * @param token token 정보
	 * @return 클레임 정보
	 */
	public Optional<Claims> resolve(String token) {
		try {
			return Optional.ofNullable(
					Jwts.parserBuilder()
							.setSigningKey(secretKey.getBytes())
							.build()
							.parseClaimsJws(token)
							.getBody());
		} catch (Exception e) {
			log.warn("Failed to get memberId. token: {}", token);
			return Optional.empty();
		}
	}

	/**
	 * 토큰의 memberId 정보를 추출 합니다.
	 *
	 * @param token token 정보
	 * @return memberId 정보
	 */
	public Optional<Long> resolveId(String token) {
		try {
			return Optional.ofNullable(
					Jwts.parserBuilder()
							.setSigningKey(secretKey.getBytes())
							.build()
							.parseClaimsJws(token)
							.getBody()
							.get(MEMBER_ID_CLAIM_KEY, Long.class));
		} catch (Exception e) {
			log.warn("Failed to get memberId. token: {}", token);
			return Optional.empty();
		}
	}

	/**
	 * 토큰의 memberRole 정보를 추출 합니다.
	 *
	 * @param token token 정보
	 * @return memberRole 정보
	 */
	public Optional<String> resolveRole(String token) {
		try {
			return Optional.ofNullable(
					Jwts.parserBuilder()
							.setSigningKey(secretKey.getBytes())
							.build()
							.parseClaimsJws(token)
							.getBody()
							.get(MEMBER_ROLE_CLAIM_KEY, String.class));
		} catch (Exception e) {
			log.warn("Failed to get memberId. token: {}", token);
			return Optional.empty();
		}
	}
}
