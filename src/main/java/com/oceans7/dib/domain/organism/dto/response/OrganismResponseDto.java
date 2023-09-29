package com.oceans7.dib.domain.organism.dto.response;

import com.oceans7.dib.domain.place.dto.response.SimplePlaceInformationDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganismResponseDto {
    @Schema(description = "생물 아이디", example = "0")
    private Long organismId;

    @Schema(description = "대표 이미지 URL", example = "https://first.images/1")
    private String firstImageUrl;

    @Schema(description = "국문 이름", example = "빨간 씬벵이")
    private String koreanName;

    @Schema(description = "영문 이름", example = "Striated frogfish")
    private String englishName;

    @Schema(description = "설명", example = "부산, 제주도 등 우리나라 남부 얕은 해역의 암초와 모래질로 된 바닥에 주로 서식하며, 해면체 군집, 산호초 등 다양한 환경에서 발견됩니다.")
    private String description;

    @Schema(description = "기본 생김새", example = "일반적인 몸길이는 10cm이며 눈이 작고, 입이 위를 향하며, 피부에는 작은 가시가 있습니다.")
    private String basicAppearance;

    @Schema(description = "상세 설명", example = "주로 암초 지대에 살면서 머리의 돌기를 흔들어 작은 물고기를 유인해 큰 입을 벌려 잡아 먹습니다.")
    private String detailDescription;

    @ArraySchema(schema = @Schema(description = "이미지 URL", implementation = String.class))
    private List<String> imageUrlList;

    @ArraySchema(schema = @Schema(description = "다른 생물", implementation = SimpleOrganismResponseDto.class))
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
