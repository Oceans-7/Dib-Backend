package com.oceans7.dib.domain.organism.controller;

import com.oceans7.dib.domain.organism.dto.response.OrganismResponseDto;
import com.oceans7.dib.domain.organism.dto.response.SimpleOrganismResponseDto;
import com.oceans7.dib.domain.organism.entity.HarmfulOrganism;
import com.oceans7.dib.domain.organism.entity.HarmfulOrganismImage;
import com.oceans7.dib.domain.organism.entity.MarineOrganism;
import com.oceans7.dib.domain.organism.entity.MarineOrganismImage;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismImageRepository;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismImageRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismRepository;
import com.oceans7.dib.domain.organism.service.OrganismService;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganismController.class)
public class OrganismControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrganismService organismService;

    @MockBean
    private MarineOrganismRepository marineOrganismRepository;

    @MockBean
    private MarineOrganismImageRepository marineOrganismImageRepository;

    @MockBean
    private HarmfulOrganismRepository harmfulOrganismRepository;

    @MockBean
    private HarmfulOrganismImageRepository harmfulOrganismImageRepository;

    // 해양 생물 데이터
    private List<MarineOrganism> makeMarineOrganism() {
        List<MarineOrganism> marineOrganism = MockEntity.testMarineOrganism();

        for(int i = 0; i < marineOrganism.size(); i++) {
            ReflectionTestUtils.setField(marineOrganism.get(i), "organismId", (long) i);
        }

        when(marineOrganismRepository.saveAllAndFlush(marineOrganism)).thenReturn(marineOrganism);
        return marineOrganismRepository.saveAllAndFlush(marineOrganism);
    }

    private List<MarineOrganismImage> makeMarineOrganismImage(MarineOrganism marineOrganism) {
        List<MarineOrganismImage> marineOrganismImageList = MockEntity.testMarineOrganismImage();

        for(int i = 0; i < marineOrganismImageList.size(); i++) {
            marineOrganismImageList.get(i).setMarineOrganism(marineOrganism);
            ReflectionTestUtils.setField(marineOrganismImageList.get(i), "organismImageId", (long) (i+1));
        }

        when(marineOrganismImageRepository.saveAllAndFlush(marineOrganismImageList)).thenReturn(marineOrganismImageList);
        return marineOrganismImageRepository.saveAllAndFlush(marineOrganismImageList);
    }

    // 유해 생물 데이터
    private List<HarmfulOrganism> makeHarmfulOrganism() {
        List<HarmfulOrganism> harmfulOrganism = MockEntity.testHarmfulOrganism();

        for(int i = 0; i < harmfulOrganism.size(); i++) {
            ReflectionTestUtils.setField(harmfulOrganism.get(i), "organismId", (long) i);
        }

        when(harmfulOrganismRepository.saveAllAndFlush(harmfulOrganism)).thenReturn(harmfulOrganism);
        return harmfulOrganismRepository.saveAllAndFlush(harmfulOrganism);
    }

    private List<HarmfulOrganismImage> makeHarmfulOrganismImage(HarmfulOrganism harmfulOrganism) {
        List<HarmfulOrganismImage> harmfulOrganismImageList = MockEntity.testHarmfulOrganismImage();

        for(int i = 0; i < harmfulOrganismImageList.size(); i++) {
            harmfulOrganismImageList.get(i).setHarmfulOrganism(harmfulOrganism);
            ReflectionTestUtils.setField(harmfulOrganismImageList.get(i), "organismImageId", (long) (i+1));
        }

        when(harmfulOrganismImageRepository.saveAllAndFlush(harmfulOrganismImageList)).thenReturn(harmfulOrganismImageList);
        return harmfulOrganismImageRepository.saveAllAndFlush(harmfulOrganismImageList);
    }

    @Test
    @DisplayName("해양 생물 리스트 조회")
    @WithMockUser()
    public void getAllMarineOrganism() throws Exception {
        // given
        List<MarineOrganism> marineOrganism = makeMarineOrganism();
        List<SimpleOrganismResponseDto> mockResponse = MockResponse.testSimpleOrganismRes(marineOrganism);
        when(organismService.getAllMarineOrganism()).thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/marine-organism"));

        // then
        result.andExpect(status().isOk());
        for(int i = 0; i < mockResponse.size(); i++) {
            result
                    .andExpect(jsonPath("$.data[" + i + "].organismId").value(mockResponse.get(i).getOrganismId()))
                    .andExpect(jsonPath("$.data[" + i + "].illustrationUrl").value(mockResponse.get(i).getIllustrationUrl()))
                    .andExpect(jsonPath("$.data[" + i + "].koreanName").value(mockResponse.get(i).getKoreanName()))
                    .andExpect(jsonPath("$.data[" + i + "].englishName").value(mockResponse.get(i).getEnglishName()))
                    .andExpect(jsonPath("$.data[" + i + "].description").value(mockResponse.get(i).getDescription()));
        }
    }

    @Test
    @DisplayName("해양 생물 상세 조회")
    @WithMockUser()
    public void getMarineOrganismDetail() throws Exception {
        // given
        List<MarineOrganism> marineOrganism = makeMarineOrganism();
        MarineOrganism detailTargetOrganism = marineOrganism.get(0);
        marineOrganism.remove(detailTargetOrganism);
        List<MarineOrganismImage> marineOrganismImageList = makeMarineOrganismImage(detailTargetOrganism);

        OrganismResponseDto mockResponse = MockResponse.testMarineOrganismRes(detailTargetOrganism, marineOrganism);
        when(organismService.getMarineOrganismDetail(detailTargetOrganism.getOrganismId())).thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/marine-organism/" + detailTargetOrganism.getOrganismId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.organismId").value(detailTargetOrganism.getOrganismId()))
                .andExpect(jsonPath("$.data.firstImageUrl").value(detailTargetOrganism.getFirstImageUrl()))
                .andExpect(jsonPath("$.data.koreanName").value(detailTargetOrganism.getKoreanName()))
                .andExpect(jsonPath("$.data.englishName").value(detailTargetOrganism.getEnglishName()))
                .andExpect(jsonPath("$.data.description").value(detailTargetOrganism.getDescription()))
                .andExpect(jsonPath("$.data.basicAppearance").value(detailTargetOrganism.getBasicAppearance()))
                .andExpect(jsonPath("$.data.detailDescription").value(detailTargetOrganism.getDetailDescription()));

        for(int i = 0; i < mockResponse.getImageUrlList().size(); i++) {
            result.andExpect(jsonPath("$.data.imageUrlList["+ i + "]").value(marineOrganismImageList.get(i).getUrl()));
        }

        for(int i = 0; i < mockResponse.getOtherOrganismList().size(); i++) {
            result.andExpect(jsonPath("$.data.otherOrganismList[" + i + "].organismId").value(mockResponse.getOtherOrganismList().get(i).getOrganismId()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].illustrationUrl").value(mockResponse.getOtherOrganismList().get(i).getIllustrationUrl()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].koreanName").value(mockResponse.getOtherOrganismList().get(i).getKoreanName()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].englishName").value(mockResponse.getOtherOrganismList().get(i).getEnglishName()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].description").value(mockResponse.getOtherOrganismList().get(i).getDescription()));
        }
    }

    @Test
    @DisplayName("유해 생물 리스트 조회")
    @WithMockUser()
    public void getAllHarmfulOrganism() throws Exception {
        // given
        List<HarmfulOrganism> harmfulOrganism = makeHarmfulOrganism();
        List<SimpleOrganismResponseDto> mockResponse = MockResponse.testSimpleOrganismRes(harmfulOrganism);
        when(organismService.getAllHarmfulOrganism()).thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/harmful-organism"));

        // then
        result.andExpect(status().isOk());
        for(int i = 0; i < mockResponse.size(); i++) {
            result
                    .andExpect(jsonPath("$.data[" + i + "].organismId").value(mockResponse.get(i).getOrganismId()))
                    .andExpect(jsonPath("$.data[" + i + "].illustrationUrl").value(mockResponse.get(i).getIllustrationUrl()))
                    .andExpect(jsonPath("$.data[" + i + "].koreanName").value(mockResponse.get(i).getKoreanName()))
                    .andExpect(jsonPath("$.data[" + i + "].englishName").value(mockResponse.get(i).getEnglishName()))
                    .andExpect(jsonPath("$.data[" + i + "].description").value(mockResponse.get(i).getDescription()));
        }
    }

    @Test
    @DisplayName("해양 생물 상세 조회")
    @WithMockUser()
    public void getHarmfulOrganismDetail() throws Exception {
        // given
        List<HarmfulOrganism> harmfulOrganism = makeHarmfulOrganism();
        HarmfulOrganism detailTargetOrganism = harmfulOrganism.get(0);
        harmfulOrganism.remove(detailTargetOrganism);
        List<HarmfulOrganismImage> harmfulOrganismImageList = makeHarmfulOrganismImage(detailTargetOrganism);

        OrganismResponseDto mockResponse = MockResponse.testHarmfulOrganismRes(detailTargetOrganism, harmfulOrganism);
        when(organismService.getHarmfulOrganismDetail(detailTargetOrganism.getOrganismId())).thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/harmful-organism/" + detailTargetOrganism.getOrganismId()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.organismId").value(detailTargetOrganism.getOrganismId()))
                .andExpect(jsonPath("$.data.firstImageUrl").value(detailTargetOrganism.getFirstImageUrl()))
                .andExpect(jsonPath("$.data.koreanName").value(detailTargetOrganism.getKoreanName()))
                .andExpect(jsonPath("$.data.englishName").value(detailTargetOrganism.getEnglishName()))
                .andExpect(jsonPath("$.data.description").value(detailTargetOrganism.getDescription()))
                .andExpect(jsonPath("$.data.basicAppearance").value(detailTargetOrganism.getBasicAppearance()))
                .andExpect(jsonPath("$.data.detailDescription").value(detailTargetOrganism.getDetailDescription()));

        for(int i = 0; i < mockResponse.getImageUrlList().size(); i++) {
            result.andExpect(jsonPath("$.data.imageUrlList["+ i + "]").value(harmfulOrganismImageList.get(i).getUrl()));
        }

        for(int i = 0; i < mockResponse.getOtherOrganismList().size(); i++) {
            result.andExpect(jsonPath("$.data.otherOrganismList[" + i + "].organismId").value(mockResponse.getOtherOrganismList().get(i).getOrganismId()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].illustrationUrl").value(mockResponse.getOtherOrganismList().get(i).getIllustrationUrl()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].koreanName").value(mockResponse.getOtherOrganismList().get(i).getKoreanName()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].englishName").value(mockResponse.getOtherOrganismList().get(i).getEnglishName()))
                    .andExpect(jsonPath("$.data.otherOrganismList[" + i + "].description").value(mockResponse.getOtherOrganismList().get(i).getDescription()));
        }
    }
}
