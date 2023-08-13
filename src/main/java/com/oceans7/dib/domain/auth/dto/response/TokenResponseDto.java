package com.oceans7.dib.domain.auth.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TokenResponseDto {

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    public static TokenResponseDto of(String accessToken, String refreshToken) {
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.accessToken = accessToken;
        tokenResponseDto.refreshToken = refreshToken;

        return tokenResponseDto;
    }
}
