package com.oceans7.dib.domain.custom_content.controller;

import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.custom_content.repository.CustomContentRepository;
import com.oceans7.dib.domain.custom_content.service.CustomContentService;
import com.oceans7.dib.global.MockEntity;
import com.oceans7.dib.global.MockResponse;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(CustomContentController.class)
public class CustomContentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomContentService customContentService;

    @MockBean
    private CustomContentRepository customContentRepository;

    private CustomContent customContent;

    @BeforeEach
    public void before() {
        customContent = makeCustomContent();
    }

    private CustomContent makeCustomContent() {
        CustomContent customContent = MockEntity.testCustomContent();

        ReflectionTestUtils.setField(customContent, "customContentId", 1L);

        when(customContentRepository.save(customContent)).thenReturn(customContent);
        return customContentRepository.save(customContent);
    }

    @Test
    @DisplayName("자체 콘텐츠 리스트 조회")
    @WithMockUser()
    public void getAllCustomContent() throws Exception {
        // given
        List<ContentResponseDto> mockResponse = MockResponse.testCustomContentRes(customContent);
        when(customContentService.getAllCustomContent())
                .thenReturn(mockResponse);

        // when
        ResultActions result = mvc.perform(get("/content"));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].customContentId").value(mockResponse.get(0).getCustomContentId()))
                .andExpect(jsonPath("$.data[0].firstImage").value(mockResponse.get(0).getFirstImage()))
                .andExpect(jsonPath("$.data[0].title").value(mockResponse.get(0).getTitle()))
                .andExpect(jsonPath("$.data[0].subTitle").value(mockResponse.get(0).getSubTitle()));
    }
}
