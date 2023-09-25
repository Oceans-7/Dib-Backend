package com.oceans7.dib.domain.organism.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleOrganismResponseDto {
    private Long organismId;

    private String illustrationUrl;

    private String koreanName;

    private String englishName;

    private String description;

    public static SimpleOrganismResponseDto of(Long organismId, String illustrationUrl, String koreanName, String englishName, String description) {
        SimpleOrganismResponseDto simpleOrganism = new SimpleOrganismResponseDto();

        simpleOrganism.organismId = organismId;
        simpleOrganism.illustrationUrl = illustrationUrl;
        simpleOrganism.koreanName = koreanName;
        simpleOrganism.englishName = englishName;
        simpleOrganism.description = description;

        return simpleOrganism;
    }
}
