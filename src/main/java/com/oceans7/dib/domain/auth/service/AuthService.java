package com.oceans7.dib.domain.auth.service;

import com.oceans7.dib.domain.auth.dto.request.KakaoLoginRequestDto;
import com.oceans7.dib.domain.auth.dto.response.TokenResponseDto;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.domain.user_refresh_token.entity.UserRefreshToken;
import com.oceans7.dib.domain.user_refresh_token.repository.UserRefreshTokenRepository;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.JwtTokenUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    private final UserRefreshTokenRepository userRefreshTokenRepository;

    @Value("${kakao.auth.jwt.aud}")
    public String aud;

    @Value("${kakao.auth.jwt.iss}")
    public String iss;

    @Transactional
    public TokenResponseDto kakaoLogin(KakaoLoginRequestDto kakaoLoginRequestDto) {


        String idToken = kakaoLoginRequestDto.getIdToken();
        Jwt<Header, Claims> claims = jwtTokenUtil.parseJwt(idToken);

        String kid = claims.getHeader().get("kid").toString();
        jwtTokenUtil.verifySignature(idToken, kid, aud, iss, kakaoLoginRequestDto.getNonce());

        String nickname = claims.getBody().get("nickname", String.class);
        String picture = claims.getBody().get("picture", String.class);
        String socialUserId = claims.getBody().getSubject();

        User user = upsertUser(SocialType.KAKAO, socialUserId, nickname, picture);

        String accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user);
        String refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user);

        userRefreshTokenRepository.save(UserRefreshToken.of(refreshToken, user));

        return TokenResponseDto.of(accessToken, refreshToken);
    }

    private User upsertUser(SocialType socialType, String socialUserId, String nickname, String picture) {
        return userRepository.findBySocialTypeAndSocialUserId(socialType, socialUserId)
                .orElseGet(() -> userRepository.save(User.of(picture, nickname, SocialType.KAKAO, socialUserId, Role.USER)));
    }

    @Transactional
    public TokenResponseDto regenerateToken(String token) {
        Jws<Claims> claims = jwtTokenUtil.parseToken(token);
        TokenType tokenType = TokenType.valueOf(claims.getBody().get("type", String.class));

        if (tokenType != TokenType.REFRESH_TOKEN) {
            throw new ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION);
        }

        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new ApplicationException(ErrorCode.TOKEN_NOT_FOUND));

        String subject = claims.getBody().getSubject();
        User user = userRepository.findById(Long.parseLong(subject))
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));
        String accessToken = jwtTokenUtil.generateToken(TokenType.ACCESS_TOKEN, user);
        String refreshToken = jwtTokenUtil.generateToken(TokenType.REFRESH_TOKEN, user);

        userRefreshToken.updateRefreshToken(refreshToken);

        return TokenResponseDto.of(accessToken, refreshToken);
    }
}
