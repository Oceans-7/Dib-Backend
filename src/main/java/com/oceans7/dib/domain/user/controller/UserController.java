package com.oceans7.dib.domain.user.controller;

import com.oceans7.dib.domain.auth.user_account.UserAccount;
import com.oceans7.dib.domain.user.dto.response.UserInfoResponseDto;
import com.oceans7.dib.global.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "user", description = "유저 API")
@RequestMapping("/user")
@RequiredArgsConstructor
@RestController
public class UserController {


    @GetMapping("/me")
    @Operation(summary = "내 정보 조회", description = "토큰에 해당하는 유저의 정보 조회")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal UserAccount userAccount) {

        if (userAccount == null) {
            return ResponseEntity.ok(new UserInfoResponseDto());
        }
        return ResponseEntity.ok(UserInfoResponseDto.from(userAccount));
    }
}
