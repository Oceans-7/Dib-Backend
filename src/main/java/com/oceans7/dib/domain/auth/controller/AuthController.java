package com.oceans7.dib.domain.auth.controller;

import com.oceans7.dib.domain.auth.dto.request.SocialLoginRequestDto;
import com.oceans7.dib.domain.auth.service.AuthService;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService AuthService;

    @GetMapping("login")
    @Operation(summary = "카카오 로그인 API", description = "카카오 idtoken을 입력받아 소셜 로그인을 진행")
    public ApplicationResponse<?> kakaoLogin(
            @Validated @ModelAttribute SocialLoginRequestDto socialLoginRequestDto
    ) {
        return ApplicationResponse.ok(AuthService.login(socialLoginRequestDto));
    }
}
