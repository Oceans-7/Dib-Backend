package com.oceans7.dib.domain.auth.service;

import com.oceans7.dib.domain.auth.dto.request.SocialLoginRequestDto;
import com.oceans7.dib.domain.auth.dto.response.TokenResponseDto;
import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import com.oceans7.dib.domain.user.repository.UserRepository;
import com.oceans7.dib.global.api.http.KakaoAuthApi;
import com.oceans7.dib.global.api.response.kakaoAuth.OpenKeyListResponse;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.JwtTokenUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoAuthApi kakaoAuthApi;

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    public static String AUD = "kakao";
    public static String ISS = "https://kauth.kakao.com/oauth/authorize";

    public TokenResponseDto login(SocialLoginRequestDto socialLoginRequestDto) {


        String idToken = socialLoginRequestDto.getIdToken();
        Jws<Claims> claims = parseJwt(idToken, AUD, ISS, socialLoginRequestDto.getNonce());

        String kid = claims.getHeader().get("kid").toString();
        verifySignature(idToken, kid);

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
                .orElseGet(() -> userRepository.save(User.of(picture, nickname, SocialType.KAKAO, socialUserId)));
    }

    public Jws<Claims> parseJwt(String jwt, String aud, String iss, String nonce) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .requireAudience(aud)
                    .requireIssuer(iss)
                    .parseClaimsJws(jwt);
            if (nonce.equals(claims.getBody().get("nonce", String.class))) {
                throw new ApplicationException(ErrorCode.NONCE_NOT_MATCHED);
            }
            return claims;

        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            throw new ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION);
        }
    }

    public void verifySignature(String idToken, String kid){

        OpenKeyListResponse keyListResponse = kakaoAuthApi.getKakaoOpenKeyAddress();


        //TODO openKey 값을 캐싱해서 사용할 수 있도록 수정
        OpenKeyListResponse.JWK openKey = keyListResponse.getKeys().stream()
                .filter(key -> key.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.OPENKEY_NOT_MATCHED));

        try {
            Jwts.parser()
                    .setSigningKey(getRSAPublicKey(openKey.getN(), openKey.getE()))
                    .parseClaimsJws(idToken);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApplicationException(ErrorCode.TOKEN_VERIFICATION_EXCEPTION);
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
}
