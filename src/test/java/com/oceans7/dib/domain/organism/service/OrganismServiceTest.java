package com.oceans7.dib.domain.organism.service;

import com.oceans7.dib.domain.organism.dto.response.OrganismResponseDto;
import com.oceans7.dib.domain.organism.dto.response.SimpleOrganismResponseDto;
import com.oceans7.dib.domain.organism.entity.*;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismImageRepository;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismImageRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismRepository;
import com.oceans7.dib.global.MockEntity;
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
        for(int i = 0; i < response.size(); i++) {
            assertThat(response.get(i).getOrganismId()).isEqualTo(marineOrganism.get(i).getOrganismId());
            assertThat(response.get(i).getIllustrationUrl()).isEqualTo(marineOrganism.get(i).getIllustrationImageUrl());
            assertThat(response.get(i).getKoreanName()).isEqualTo(marineOrganism.get(i).getKoreanName());
            assertThat(response.get(i).getEnglishName()).isEqualTo(marineOrganism.get(i).getEnglishName());
            assertThat(response.get(i).getDescription()).isEqualTo(marineOrganism.get(i).getDescription());
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
        assertThat(response.getOrganismId()).isEqualTo(marineOrganism.get(0).getOrganismId());
        assertThat(response.getFirstImageUrl()).isEqualTo(marineOrganism.get(0).getFirstImageUrl());
        assertThat(response.getKoreanName()).isEqualTo(marineOrganism.get(0).getKoreanName());
        assertThat(response.getEnglishName()).isEqualTo(marineOrganism.get(0).getEnglishName());
        assertThat(response.getDescription()).isEqualTo(marineOrganism.get(0).getDescription());
        assertThat(response.getBasicAppearance()).isEqualTo(marineOrganism.get(0).getBasicAppearance());
        assertThat(response.getDetailDescription()).isEqualTo(marineOrganism.get(0).getDetailDescription());

        // 해양 생물 이미지
        for(int i = 0; i < marineOrganismImageList.size(); i++) {
            assertThat(response.getImageUrlList().get(i))
                    .isEqualTo(marineOrganism.get(0).getMarineOrganismImageList().get(i).getUrl());
        }

        // 다른 해양 생물
        assertThat(response.getOtherOrganismList().size()).isEqualTo(marineOrganism.size()-1);

        for(int i = 0; i < marineOrganism.size()-1; i++) {
            assertThat(response.getOtherOrganismList().get(i).getOrganismId()).isEqualTo(marineOrganism.get(i+1).getOrganismId());
            assertThat(response.getOtherOrganismList().get(i).getIllustrationUrl()).isEqualTo(marineOrganism.get(i+1).getIllustrationImageUrl());
            assertThat(response.getOtherOrganismList().get(i).getKoreanName()).isEqualTo(marineOrganism.get(i+1).getKoreanName());
            assertThat(response.getOtherOrganismList().get(i).getEnglishName()).isEqualTo(marineOrganism.get(i+1).getEnglishName());
            assertThat(response.getOtherOrganismList().get(i).getDescription()).isEqualTo(marineOrganism.get(i+1).getDescription());        }
    }

    @Test
    @DisplayName("유해 생물 전체 조회")
    public void getAllHarmfulOrganism() {
        // given
        List<HarmfulOrganism> harmfulOrganism = makeHarmfulOrganism();

        // when
        List<SimpleOrganismResponseDto> response = organismService.getAllHarmfulOrganism();

        // then
        for(int i = 0; i < response.size(); i++) {
            assertThat(response.get(i).getOrganismId()).isEqualTo(harmfulOrganism.get(i).getOrganismId());
            assertThat(response.get(i).getIllustrationUrl()).isEqualTo(harmfulOrganism.get(i).getIllustrationImageUrl());
            assertThat(response.get(i).getKoreanName()).isEqualTo(harmfulOrganism.get(i).getKoreanName());
            assertThat(response.get(i).getEnglishName()).isEqualTo(harmfulOrganism.get(i).getEnglishName());
            assertThat(response.get(i).getDescription()).isEqualTo(harmfulOrganism.get(i).getDescription());
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
        assertThat(response.getOrganismId()).isEqualTo(harmfulOrganism.get(0).getOrganismId());
        assertThat(response.getFirstImageUrl()).isEqualTo(harmfulOrganism.get(0).getFirstImageUrl());
        assertThat(response.getKoreanName()).isEqualTo(harmfulOrganism.get(0).getKoreanName());
        assertThat(response.getEnglishName()).isEqualTo(harmfulOrganism.get(0).getEnglishName());
        assertThat(response.getDescription()).isEqualTo(harmfulOrganism.get(0).getDescription());
        assertThat(response.getBasicAppearance()).isEqualTo(harmfulOrganism.get(0).getBasicAppearance());
        assertThat(response.getDetailDescription()).isEqualTo(harmfulOrganism.get(0).getDetailDescription());

        // 해양 생물 이미지
        for(int i = 0; i < harmfulOrganismImageList.size(); i++) {
            assertThat(response.getImageUrlList().get(i))
                    .isEqualTo(harmfulOrganism.get(0).getHarmfulOrganismImageList().get(i).getUrl());
        }

        // 다른 해양 생물
        assertThat(response.getOtherOrganismList().size()).isEqualTo(harmfulOrganism.size()-1);

        for(int i = 0; i < harmfulOrganism.size()-1; i++) {
            assertThat(response.getOtherOrganismList().get(i).getOrganismId()).isEqualTo(harmfulOrganism.get(i+1).getOrganismId());
            assertThat(response.getOtherOrganismList().get(i).getIllustrationUrl()).isEqualTo(harmfulOrganism.get(i+1).getIllustrationImageUrl());
            assertThat(response.getOtherOrganismList().get(i).getKoreanName()).isEqualTo(harmfulOrganism.get(i+1).getKoreanName());
            assertThat(response.getOtherOrganismList().get(i).getEnglishName()).isEqualTo(harmfulOrganism.get(i+1).getEnglishName());
            assertThat(response.getOtherOrganismList().get(i).getDescription()).isEqualTo(harmfulOrganism.get(i+1).getDescription());        }
    }
}
