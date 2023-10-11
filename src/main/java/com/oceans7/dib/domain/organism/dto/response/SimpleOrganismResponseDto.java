package com.oceans7.dib.domain.organism.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleOrganismResponseDto {
    @Schema(description = "생물 아이디", example = "0")
    private Long organismId;

    @Schema(description = "일러스트 이미지 URL", example = "https://illustration.images/1")
    private String illustrationUrl;

    @Schema(description = "국문 이름", example = "빨간 씬벵이")
    private String koreanName;

    @Schema(description = "영문 이름", example = "Striated frogfish")
    private String englishName;

    @Schema(description = "설명", example = "부산, 제주도 등 우리나라 남부 얕은 해역의 암초와 모래질로 된 바닥에 주로 서식하며, 해면체 군집, 산호초 등 다양한 환경에서 발견됩니다.")
    private String description;

    @Schema(description = "신고 전화번호(해양 생물인 경우 null로 전달)", example = "122")
    private String reportNumber;

    public static SimpleOrganismResponseDto of(Long organismId, String illustrationUrl, String koreanName, String englishName, String description) {
        SimpleOrganismResponseDto simpleOrganism = new SimpleOrganismResponseDto();

        simpleOrganism.organismId = organismId;
        simpleOrganism.illustrationUrl = illustrationUrl;
        simpleOrganism.koreanName = koreanName;
        simpleOrganism.englishName = englishName;
        simpleOrganism.description = description;

        return simpleOrganism;
    }

    public static SimpleOrganismResponseDto of(Long organismId, String illustrationUrl, String koreanName, String englishName, String description, String reportNumber) {
        SimpleOrganismResponseDto simpleOrganism = new SimpleOrganismResponseDto();

        simpleOrganism.organismId = organismId;
        simpleOrganism.illustrationUrl = illustrationUrl;
        simpleOrganism.koreanName = koreanName;
        simpleOrganism.englishName = englishName;
        simpleOrganism.description = description;
        simpleOrganism.reportNumber = reportNumber;

        return simpleOrganism;
    }
}
