package com.oceans7.dib.domain.custom_content.service;

import com.oceans7.dib.domain.custom_content.dto.response.ContentResponseDto;
import com.oceans7.dib.domain.custom_content.dto.response.detail.Content;
import com.oceans7.dib.domain.custom_content.dto.response.detail.DetailContentResponseDto;
import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import com.oceans7.dib.domain.custom_content.repository.CustomContentRepository;
import com.oceans7.dib.global.api.service.JsonParsingService;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomContentService extends JsonParsingService {

    private final CustomContentRepository customContentRepository;

    @Transactional(readOnly = true)
    public List<ContentResponseDto> getAllCustomContent() {
        List<CustomContent> customContentList = customContentRepository.findAll();

        return customContentList.stream()
                .map(content -> ContentResponseDto.of(
                        content.getCustomContentId(),
                        content.getCoverImageUrl(),
                        content.getTitle(),
                        content.getSubTitle()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DetailContentResponseDto getDetailCustomContent(Long customContentId) {
        CustomContent customContent = customContentRepository.findById(customContentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION));

        return DetailContentResponseDto.of(
                customContent.getCustomContentId(),
                parseDetailContentResponseDto(customContent.getJsonContent())
        );
    }

    private Content parseDetailContentResponseDto(String json) {
        return parsingJsonObject(json, Content.class);
    }
}
