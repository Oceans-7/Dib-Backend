package com.oceans7.dib.domain.custom_content.dto.response.detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailContentResponseDto {
    @Schema(description = "자체 컨텐츠 아이디", example = "0")
    private Long customContentId;

    @Schema(description = "자체 컨텐츠 제목", example = "제주 서귀포 콘텐츠")
    private String contentTitle;

    @Schema(description = "자체 컨텐츠", implementation = Content.class)
    private Content content;

    public static DetailContentResponseDto of(Long customContentId, String contentTitle, Content content) {
        DetailContentResponseDto customContentResponseDto = new DetailContentResponseDto();

        customContentResponseDto.customContentId = customContentId;
        customContentResponseDto.contentTitle = contentTitle;
        customContentResponseDto.content = content;

        return customContentResponseDto;
    }
}
