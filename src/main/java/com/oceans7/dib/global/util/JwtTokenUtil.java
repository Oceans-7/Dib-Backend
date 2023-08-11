package com.oceans7.dib.global.util;


import com.oceans7.dib.domain.auth.service.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    //TODO 환경 변수로 빼기
    private static final int ACCESS_TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000;

    private static final int REFRESH_TOKEN_EXPIRATION_MS = 14 * 24 * 60 * 60 * 1000;

    // jwt 토큰 생성
    public String generateAccessToken(TokenType tokenType, Long userId, String profileUrl) {
        Date now = new Date();

        int expireDuration = tokenType == TokenType.REFRESH_TOKEN ? REFRESH_TOKEN_EXPIRATION_MS : ACCESS_TOKEN_EXPIRATION_MS;

        Date expiryDate = new Date(now.getTime() + expireDuration);
        Claims claims = Jwts.claims()
                .setSubject(userId.toString()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                ;
        claims.put("user_id", userId);
        claims.put("profileUrl", profileUrl);
        claims.put("type", tokenType);

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        return token;
    }
}