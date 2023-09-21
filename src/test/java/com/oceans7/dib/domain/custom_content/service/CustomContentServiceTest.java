package com.oceans7.dib.domain.custom_content.service;

import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.custom_content.repository.CustomContentRepository;
import com.oceans7.dib.global.MockRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CustomContentServiceTest {
    @Autowired
    private CustomContentService customContentService;

    @Autowired
    private CustomContentRepository customContentRepository;

    private CustomContent makeCustomContent() {
        return customContentRepository.save(MockRequest.testCustomContent());
    }

    @Test
    @DisplayName("자체 콘텐츠 리스트 조회")
    public void getAllCustomContent() {
        // given
        CustomContent customContent = makeCustomContent();

        // when
        List<ContentResponseDto> response = customContentService.getAllCustomContent();

        // then
        assertThat(response.get(0).getCustomContentId()).isEqualTo(customContent.getCustomContentId());
        assertThat(response.get(0).getTitle()).isEqualTo(customContent.getTitle());
        assertThat(response.get(0).getSubTitle()).isEqualTo(customContent.getSubTitle());
        assertThat(response.get(0).getFirstImage()).isEqualTo(customContent.getCoverImageUrl());
    }
}
