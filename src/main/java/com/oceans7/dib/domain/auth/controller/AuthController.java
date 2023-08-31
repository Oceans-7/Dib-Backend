package com.oceans7.dib.domain.auth.controller;

import com.oceans7.dib.domain.auth.dto.request.KakaoLoginRequestDto;
import com.oceans7.dib.domain.auth.dto.response.TokenResponseDto;
import com.oceans7.dib.domain.auth.service.AuthService;
import com.oceans7.dib.global.response.ApplicationResponse;
import com.oceans7.dib.global.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao-login")
    @Operation(summary = "카카오 로그인 API", description = "카카오 idtoken을 입력받아 소셜 로그인을 진행")
    public ApplicationResponse<TokenResponseDto> kakaoLogin(
            @Validated @RequestBody KakaoLoginRequestDto kakaoLoginRequestDto
    ) {
        return ApplicationResponse.ok(authService.kakaologin(kakaoLoginRequestDto));
    }

    @PostMapping("/regenerated-token")
    @Operation(summary = "토큰 재발급 API", description = "기존 토큰을 입력받아 새로운 토큰을 발급")
    public ApplicationResponse<TokenResponseDto> regeneratedToken(
            @Schema(hidden = true) @NotNull @RequestHeader("Authorization") String authorizationHeader
    ) {
        String token = CommonUtil.parseTokenFromBearer(authorizationHeader);

        TokenResponseDto tokenResponseDto = authService.regenerateToken(token);

        return ApplicationResponse.ok(tokenResponseDto);
    }
}
