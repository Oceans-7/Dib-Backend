package com.oceans7.dib.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PartnerSectionResponseDto {
    @Schema(description = "파트너 섹션 제목", example = "제주 서귀포 숙박, 다이빙 체험 할인 참여 업체")
    private String title;

    @Schema(description = "하이라이트 키워드(구분자 : ',')", example = "숙박, 다이닝")
    private String keyword;

    @Schema(description = "협력 업체 1번 섹션 정보", implementation = PartnerResponseDto.class)
    private PartnerResponseDto firstPartner;

    @Schema(description = "협력 업체 2번 섹션 정보", implementation = PartnerResponseDto.class)
    private PartnerResponseDto secondPartner;

    public static PartnerSectionResponseDto of(String title, String keyword, PartnerResponseDto firstPartner, PartnerResponseDto secondPartner) {
        PartnerSectionResponseDto partnerSectionResponseDto = new PartnerSectionResponseDto();

        partnerSectionResponseDto.title = title;
        partnerSectionResponseDto.keyword = keyword;
        partnerSectionResponseDto.firstPartner = firstPartner;
        partnerSectionResponseDto.secondPartner = secondPartner;

        return partnerSectionResponseDto;
    }
}
