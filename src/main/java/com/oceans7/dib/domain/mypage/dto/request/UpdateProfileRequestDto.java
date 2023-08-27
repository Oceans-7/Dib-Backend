package com.oceans7.dib.domain.mypage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateProfileRequestDto {
    @Schema(description = "변경 닉네임", example = "김다이브")
    private String nickname;

    @Schema(description = "변경 프로필 사진 파일")
    private MultipartFile multipartFile;
}
