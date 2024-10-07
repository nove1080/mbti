package com.example.demo.support.token;

import com.example.demo.security.authentication.authority.Roles;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenGenerator {

	@Value("${security.jwt.token.secretkey}")
	private String secretKey;

	@Value("${security.jwt.token.validtime.access}")
	private Long accessTokenValidTime;

	@Value("${security.jwt.token.validtime.refresh}")
	private Long refreshTokenValidTime;

	private static final String MEMBER_ID_CLAIM_KEY = "memberId";
	private static final String MEMBER_ROLE_CLAIM_KEY = "memberRole";

	/**
	 * id와 role을 이용하여 AuthToken을 생성한다.
	 *
	 * @param memberId id 정보
	 * @param memberRoles role 정보
	 * @return AuthToken
	 */
	public AuthToken generateAuthToken(Long memberId, List<Roles> memberRoles) {
		return AuthToken.builder()
				.accessToken(generateAccessToken(memberId, memberRoles))
				.refreshToken(generateRefreshToken(memberId, memberRoles))
				.build();
	}

	/**
	 * id와 role을 이용하여 AccessToken을 생성한다.
	 *
	 * @param memberId id 정보
	 * @param memberRoles role 정보
	 * @return AccessToken
	 */
	public String generateAccessToken(Long memberId, List<Roles> memberRoles) {
		Date now = new Date();
		List<String> roles = convertToStringList(memberRoles);

		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.claim(MEMBER_ID_CLAIM_KEY, memberId)
				.claim(MEMBER_ROLE_CLAIM_KEY, roles.toString())
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + accessTokenValidTime))
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.compact();
	}

	/**
	 * id와 role을 이용하여 RefreshToken을 생성한다.
	 *
	 * @param memberId id 정보
	 * @param memberRoles role 정보
	 * @return RefreshToken
	 */
	String generateRefreshToken(Long memberId, List<Roles> memberRoles) {
		Date now = new Date();
		List<String> roles = convertToStringList(memberRoles);

		return Jwts.builder()
				.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
				.claim(MEMBER_ID_CLAIM_KEY, memberId)
				.claim(MEMBER_ROLE_CLAIM_KEY, roles.toString())
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + refreshTokenValidTime))
				.signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.compact();
	}

	private static List<String> convertToStringList(List<Roles> memberRoles) {
		List<String> stringRoles = new ArrayList<>();
		for (Roles role : memberRoles) {
			stringRoles.add(role.getRole());
		}
		return stringRoles;
	}
}
