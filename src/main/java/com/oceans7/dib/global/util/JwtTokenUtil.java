package com.oceans7.dib.global.util;


import com.oceans7.dib.domain.auth.service.TokenType;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.global.api.http.KakaoAuthApi;
import com.oceans7.dib.global.api.response.kakaoAuth.OpenKeyListResponse;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final KakaoAuthApi kakaoAuthApi;

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    //TODO 환경 변수로 빼기
    private static final int ACCESS_TOKEN_EXPIRATION_MS = 24 * 60 * 60 * 1000;

    private static final int REFRESH_TOKEN_EXPIRATION_MS = 14 * 24 * 60 * 60 * 1000;

    // jwt 토큰 생성
    public String generateToken(TokenType tokenType, User user) {
        Date now = new Date();

        int expireDuration = tokenType == TokenType.REFRESH_TOKEN ? REFRESH_TOKEN_EXPIRATION_MS : ACCESS_TOKEN_EXPIRATION_MS;

        Date expiryDate = new Date(now.getTime() + expireDuration);
        Claims claims = Jwts.claims()
                .setSubject(user.getId().toString()) // 사용자
                .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
                .setExpiration(expiryDate) // 만료 시간 세팅
                ;
        claims.put("user_id", user.getId());
        claims.put("nick_name", user.getNickname());
        claims.put("profile_url", user.getProfileUrl());
        claims.put("type", tokenType);
        claims.put("role", user.getRole());
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();

        return token;
    }

    private String removeSignature(String jwt) {
        String[] jwtSplit = jwt.split("\\.");
        return jwtSplit[0] + "." + jwtSplit[1] + ".";
    }

    public Jwt<Header, Claims> parseJwt(String jwt) {
        try {
            String jwtWithoutSignature = removeSignature(jwt);

            return Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(jwtWithoutSignature);

        } catch (ExpiredJwtException | MalformedJwtException e) {
            throw new ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION);
        }
    }

    public void verifySignature(String idToken, String kid, String aud, String iss, String nonce){

        OpenKeyListResponse keyListResponse = kakaoAuthApi.getKakaoOpenKeyAddress();


        //TODO openKey 값을 캐싱해서 사용할 수 있도록 수정
        OpenKeyListResponse.JWK openKey = keyListResponse.getKeys().stream()
                .filter(key -> key.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.OPENKEY_NOT_MATCHED));

        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .setSigningKey(getRSAPublicKey(openKey.getN(), openKey.getE()))
                    .build()
                    .parseClaimsJws(idToken);

            if (!nonce.equals(claims.getBody().get("nonce", String.class))) {
                throw new ApplicationException(ErrorCode.NONCE_NOT_MATCHED);
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION);
        }
    }

    public Jws<Claims> parseToken(String token) {
        try {

            return Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            throw new ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION);
        }
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            return false;
        }
    }

    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }

    public Long extractUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token).getBody();
            return claims.get("user_id", Long.class);
        } catch (JwtException e) {
            return null;
        }
    }

}