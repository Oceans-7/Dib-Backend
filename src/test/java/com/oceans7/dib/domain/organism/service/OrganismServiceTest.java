package com.oceans7.dib.domain.organism.service;

import com.oceans7.dib.domain.organism.dto.response.OrganismResponseDto;
import com.oceans7.dib.domain.organism.dto.response.SimpleOrganismResponseDto;
import com.oceans7.dib.domain.organism.entity.*;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismImageRepository;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismImageRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismRepository;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrganismServiceTest {
    @Autowired
    private OrganismService organismService;

    @Autowired
    private MarineOrganismRepository marineOrganismRepository;

    @Autowired
    private MarineOrganismImageRepository marineOrganismImageRepository;

    @Autowired
    private HarmfulOrganismRepository harmfulOrganismRepository;

    @Autowired
    private HarmfulOrganismImageRepository harmfulOrganismImageRepository;

    // 해양 생물 데이터
    private List<MarineOrganism> makeMarineOrganism() {
        List<MarineOrganism> marineOrganism = MockEntity.testMarineOrganism();
        return marineOrganismRepository.saveAllAndFlush(marineOrganism);
    }

    private List<MarineOrganismImage> makeMarineOrganismImage(MarineOrganism marineOrganism) {
        List<MarineOrganismImage> marineOrganismImageList = MockEntity.testMarineOrganismImage();

        for(MarineOrganismImage image : marineOrganismImageList) {
            image.setMarineOrganism(marineOrganism);
        }

        return marineOrganismImageRepository.saveAllAndFlush(marineOrganismImageList);
    }

    // 유해 생물 데이터
    private List<HarmfulOrganism> makeHarmfulOrganism() {
        List<HarmfulOrganism> harmfulOrganism = MockEntity.testHarmfulOrganism();
        return harmfulOrganismRepository.saveAllAndFlush(harmfulOrganism);
    }

    private List<HarmfulOrganismImage> makeHarmfulOrganismImage(HarmfulOrganism harmfulOrganism) {
        List<HarmfulOrganismImage> harmfulOrganismImageList = MockEntity.testHarmfulOrganismImage();

        for(HarmfulOrganismImage image : harmfulOrganismImageList) {
            image.setHarmfulOrganism(harmfulOrganism);
        }

        return harmfulOrganismImageRepository.saveAllAndFlush(harmfulOrganismImageList);
    }

    @Test
    @DisplayName("해양 생물 전체 조회")
    public void getAllMarineOrganism() {
        // given
        List<MarineOrganism> marineOrganism = makeMarineOrganism();

        // when
        List<SimpleOrganismResponseDto> response = organismService.getAllMarineOrganism();

        // then
        List<SimpleOrganismResponseDto> mockResponse = MockResponse.testSimpleOrganismRes(marineOrganism);
        for(int i = 0; i < response.size(); i++) {
            assertThat(response.get(i).getOrganismId()).isEqualTo(mockResponse.get(i).getOrganismId());
            assertThat(response.get(i).getIllustrationUrl()).isEqualTo(mockResponse.get(i).getIllustrationUrl());
            assertThat(response.get(i).getKoreanName()).isEqualTo(mockResponse.get(i).getKoreanName());
            assertThat(response.get(i).getEnglishName()).isEqualTo(mockResponse.get(i).getEnglishName());
            assertThat(response.get(i).getDescription()).isEqualTo(mockResponse.get(i).getDescription());
        }
    }

    @Test
    @DisplayName("해양 생물 상세 조회")
    public void getMarineOrganismDetail() {
        // given
        List<MarineOrganism> marineOrganism = makeMarineOrganism();
        List<MarineOrganismImage> marineOrganismImageList = makeMarineOrganismImage(marineOrganism.get(0));

        // when
        OrganismResponseDto response = organismService.getMarineOrganismDetail(marineOrganism.get(0).getOrganismId());

        // then
        OrganismResponseDto mockResponse = MockResponse.testMarineOrganismRes(marineOrganism.get(0), marineOrganism.subList(1, marineOrganism.size()));
        assertThat(response.getOrganismId()).isEqualTo(mockResponse.getOrganismId());
        assertThat(response.getFirstImageUrl()).isEqualTo(mockResponse.getFirstImageUrl());
        assertThat(response.getKoreanName()).isEqualTo(mockResponse.getKoreanName());
        assertThat(response.getEnglishName()).isEqualTo(mockResponse.getEnglishName());
        assertThat(response.getDescription()).isEqualTo(mockResponse.getDescription());
        assertThat(response.getBasicAppearance()).isEqualTo(mockResponse.getBasicAppearance());
        assertThat(response.getDetailDescription()).isEqualTo(mockResponse.getDetailDescription());

        // 해양 생물 이미지
        for(int i = 0; i < marineOrganismImageList.size(); i++) {
            assertThat(response.getImageUrlList().get(i))
                    .isEqualTo(mockResponse.getImageUrlList().get(i));
        }

        // 다른 해양 생물
        for(int i = 0; i < response.getOtherOrganismList().size(); i++) {
            assertThat(response.getOtherOrganismList().get(i).getOrganismId()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getOrganismId());
            assertThat(response.getOtherOrganismList().get(i).getIllustrationUrl()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getIllustrationUrl());
            assertThat(response.getOtherOrganismList().get(i).getKoreanName()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getKoreanName());
            assertThat(response.getOtherOrganismList().get(i).getEnglishName()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getEnglishName());
            assertThat(response.getOtherOrganismList().get(i).getDescription()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getDescription());        }
    }

    @Test
    @DisplayName("유해 생물 전체 조회")
    public void getAllHarmfulOrganism() {
        // given
        List<HarmfulOrganism> harmfulOrganism = makeHarmfulOrganism();

        // when
        List<SimpleOrganismResponseDto> response = organismService.getAllHarmfulOrganism();

        // then
        List<SimpleOrganismResponseDto> mockResponse = MockResponse.testSimpleOrganismRes(harmfulOrganism);
        for(int i = 0; i < response.size(); i++) {
            assertThat(response.get(i).getOrganismId()).isEqualTo(mockResponse.get(i).getOrganismId());
            assertThat(response.get(i).getIllustrationUrl()).isEqualTo(mockResponse.get(i).getIllustrationUrl());
            assertThat(response.get(i).getKoreanName()).isEqualTo(mockResponse.get(i).getKoreanName());
            assertThat(response.get(i).getEnglishName()).isEqualTo(mockResponse.get(i).getEnglishName());
            assertThat(response.get(i).getDescription()).isEqualTo(mockResponse.get(i).getDescription());
        }
    }

    @Test
    @DisplayName("해양 생물 상세 조회")
    public void getHarmfulOrganismDetail() {
        // given
        List<HarmfulOrganism> harmfulOrganism = makeHarmfulOrganism();
        List<HarmfulOrganismImage> harmfulOrganismImageList = makeHarmfulOrganismImage(harmfulOrganism.get(0));

        // when
        OrganismResponseDto response = organismService.getHarmfulOrganismDetail(harmfulOrganism.get(0).getOrganismId());

        // then
        OrganismResponseDto mockResponse = MockResponse.testHarmfulOrganismRes(harmfulOrganism.get(0), harmfulOrganism.subList(1, harmfulOrganism.size()));
        assertThat(response.getOrganismId()).isEqualTo(mockResponse.getOrganismId());
        assertThat(response.getFirstImageUrl()).isEqualTo(mockResponse.getFirstImageUrl());
        assertThat(response.getKoreanName()).isEqualTo(mockResponse.getKoreanName());
        assertThat(response.getEnglishName()).isEqualTo(mockResponse.getEnglishName());
        assertThat(response.getDescription()).isEqualTo(mockResponse.getDescription());
        assertThat(response.getBasicAppearance()).isEqualTo(mockResponse.getBasicAppearance());
        assertThat(response.getDetailDescription()).isEqualTo(mockResponse.getDetailDescription());

        // 해양 생물 이미지
        for(int i = 0; i < harmfulOrganismImageList.size(); i++) {
            assertThat(response.getImageUrlList().get(i))
                    .isEqualTo(mockResponse.getImageUrlList().get(i));
        }

        // 다른 해양 생물
        for(int i = 0; i < response.getOtherOrganismList().size(); i++) {
            assertThat(response.getOtherOrganismList().get(i).getOrganismId()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getOrganismId());
            assertThat(response.getOtherOrganismList().get(i).getIllustrationUrl()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getIllustrationUrl());
            assertThat(response.getOtherOrganismList().get(i).getKoreanName()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getKoreanName());
            assertThat(response.getOtherOrganismList().get(i).getEnglishName()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getEnglishName());
            assertThat(response.getOtherOrganismList().get(i).getDescription()).isEqualTo(mockResponse.getOtherOrganismList().get(i).getDescription());        }
    }
}
