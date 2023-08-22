package com.oceans7.dib.domain.auth.service;

import com.oceans7.dib.domain.auth.dto.request.KakaoLoginRequestDto;
import com.oceans7.dib.domain.auth.dto.response.TokenResponseDto;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.api.http.KakaoAuthApi;
import com.oceans7.dib.global.api.response.kakaoAuth.OpenKeyListResponse;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.JwtTokenUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    public static String AUD = "kakao";
    public static String ISS = "https://kauth.kakao.com/oauth/authorize";

    public TokenResponseDto kakaologin(KakaoLoginRequestDto kakaoLoginRequestDto) {


        String idToken = kakaoLoginRequestDto.getIdToken();
        Jws<Claims> claims = jwtTokenUtil.parseJwt(idToken);

        String kid = claims.getHeader().get("kid").toString();
        jwtTokenUtil.verifySignature(idToken, kid, AUD, ISS, kakaoLoginRequestDto.getNonce());

        String nickname = claims.getBody().get("nickname", String.class);
        String picture = claims.getBody().get("picture", String.class);
        String socialUserId = claims.getBody().getSubject();

        User user = upsertUser(SocialType.KAKAO, socialUserId, nickname, picture);

        String accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user.getId(), user.getProfileUrl());
        String refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user.getId(), user.getProfileUrl());

        return TokenResponseDto.of(accessToken, refreshToken);
    }

    private User upsertUser(SocialType socialType, String socialUserId, String nickname, String picture) {
        return userRepository.findBySocialTypeAndSocialUserId(SocialType.KAKAO, socialUserId)
                .orElseGet(() -> userRepository.save(User.of(picture, nickname, SocialType.KAKAO, socialUserId, Role.USER)));
    }
}
