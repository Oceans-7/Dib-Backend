package com.oceans7.dib.domain.auth.service;

import com.oceans7.dib.domain.auth.dto.request.KakaoLoginRequestDto;
import com.oceans7.dib.domain.auth.dto.response.TokenResponseDto;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.util.JwtTokenUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    @Value("${kakao.auth.jwt.aud}")
    public String aud;

    @Value("${kakao.auth.jwt.iss}")
    public String iss;

    public TokenResponseDto kakaologin(KakaoLoginRequestDto kakaoLoginRequestDto) {


        String idToken = kakaoLoginRequestDto.getIdToken();
        Jwt<Header, Claims> claims = jwtTokenUtil.parseJwt(idToken);

        String kid = claims.getHeader().get("kid").toString();
        jwtTokenUtil.verifySignature(idToken, kid, aud, iss, kakaoLoginRequestDto.getNonce());

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
