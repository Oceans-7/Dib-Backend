package com.oceans7.dib.domain.organism.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganismResponseDto {
    private Long organismId;

    private String firstImageUrl;

    private String koreanName;

    private String englishName;

    private String description;

    private String basicAppearance;

    private String detailDescription;

    private List<String> imageUrlList;

    private List<SimpleOrganismResponseDto> otherOrganismList;

    public static OrganismResponseDto of(Long organismId, String firstImageUrl, String koreanName, String englishName,
                                         String description, String basicAppearance, String detailDescription,
                                         List<String> imageUrlList, List<SimpleOrganismResponseDto> otherOrganismList) {
        OrganismResponseDto organism = new OrganismResponseDto();

        organism.organismId = organismId;
        organism.firstImageUrl = firstImageUrl;
        organism.koreanName = koreanName;
        organism.englishName = englishName;
        organism.description = description;
        organism.basicAppearance = basicAppearance;
        organism.detailDescription = detailDescription;
        organism.imageUrlList = imageUrlList;
        organism.otherOrganismList = otherOrganismList;

        return organism;
    }
}
