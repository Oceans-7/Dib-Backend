package com.oceans7.dib.domain.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {
    @Schema(description = "변경 닉네임", example = "김다이브")
    private String nickname;

    @Schema(description = "변경 프로필 사진 URL")
    private String imageUrl;
}
