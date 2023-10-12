package com.oceans7.dib.domain.organism.service;

import com.oceans7.dib.domain.organism.dto.response.OrganismResponseDto;
import com.oceans7.dib.domain.organism.dto.response.SimpleOrganismResponseDto;
import com.oceans7.dib.domain.organism.entity.*;
import com.oceans7.dib.domain.organism.repository.HarmfulOrganismRepository;
import com.oceans7.dib.domain.organism.repository.MarineOrganismRepository;
import com.oceans7.dib.global.base_entity.BaseImageEntity;
import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import com.oceans7.dib.global.util.ImageAssetUrlProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganismService {

    private final MarineOrganismRepository marineOrganismRepository;
    private final HarmfulOrganismRepository harmfulOrganismRepository;

    private final ImageAssetUrlProcessor imageAssetUrlProcessor;

    /**
     * 해양 생물 전체 조회
     */
    public List<SimpleOrganismResponseDto> getAllMarineOrganism() {
        List<MarineOrganism> marineOrganismList = marineOrganismRepository.findAll();

        return convertMarineOrganismToSimple(marineOrganismList);
    }

    /**
     * MarineOrganism 객체 리스트를 Simple Dto로 변환
     */
    private List<SimpleOrganismResponseDto> convertMarineOrganismToSimple(List<MarineOrganism> organismList) {
        return organismList.stream()
                .map(organism -> SimpleOrganismResponseDto.of(
                        organism.getOrganismId(),
                        imageAssetUrlProcessor.prependCloudFrontHost(organism.getIllustrationImageUrl()),
                        organism.getKoreanName(),
                        organism.getEnglishName(),
                        organism.getDescription()
                )).collect(Collectors.toList());
    }

    /**
     * 유해 생물 전체 조회
     */
    public List<SimpleOrganismResponseDto> getAllHarmfulOrganism() {
        List<HarmfulOrganism> harmfulOrganismList = harmfulOrganismRepository.findAll();

        return convertHarmfulOrganismToSimple(harmfulOrganismList);
    }

    /**
     * Harmful Organism 객체 리스트를 Simple Dto로 변환
     */
    private List<SimpleOrganismResponseDto> convertHarmfulOrganismToSimple(List<HarmfulOrganism> organismList) {
        return organismList.stream()
                .map(organism -> SimpleOrganismResponseDto.of(
                        organism.getOrganismId(),
                        imageAssetUrlProcessor.prependCloudFrontHost(organism.getIllustrationImageUrl()),
                        organism.getKoreanName(),
                        organism.getEnglishName(),
                        organism.getDescription(),
                        organism.getReportNumber()
                )).collect(Collectors.toList());
    }

    /**
     * 해양 생물 상세 조회
     */
    public OrganismResponseDto getMarineOrganismDetail(Long organismId) {
        MarineOrganism marineOrganism = marineOrganismRepository.findById(organismId)
                .orElseThrow(() -> { throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION); });

        return OrganismResponseDto.of(
                marineOrganism.getOrganismId(),
                imageAssetUrlProcessor.prependCloudFrontHost(marineOrganism.getFirstImageUrl()),
                marineOrganism.getKoreanName(),
                marineOrganism.getEnglishName(),
                marineOrganism.getDescription(),
                marineOrganism.getBasicAppearance(),
                marineOrganism.getDetailDescription(),
                extractImageUrl(marineOrganism.getMarineOrganismImageList()),
                getOtherMarineOrganism(organismId)
        );
    }

    /**
     * 다른 해양 생물 정보 조회
     */
    private List<SimpleOrganismResponseDto> getOtherMarineOrganism(Long organismId) {
        List<MarineOrganism> marineOrganismList = marineOrganismRepository.findAllByOrganismIdNot(organismId);

        return convertMarineOrganismToSimple(marineOrganismList);
    }

    /**
     * 유해 생물 상세 조회
     */
    public OrganismResponseDto getHarmfulOrganismDetail(Long organismId) {
        HarmfulOrganism harmfulOrganism = harmfulOrganismRepository.findById(organismId)
                .orElseThrow(() -> { throw new ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION); });

        return OrganismResponseDto.of(
                harmfulOrganism.getOrganismId(),
                imageAssetUrlProcessor.prependCloudFrontHost(harmfulOrganism.getFirstImageUrl()),
                harmfulOrganism.getKoreanName(),
                harmfulOrganism.getEnglishName(),
                harmfulOrganism.getDescription(),
                harmfulOrganism.getBasicAppearance(),
                harmfulOrganism.getDetailDescription(),
                extractImageUrl(harmfulOrganism.getHarmfulOrganismImageList()),
                getOtherHarmfulOrganism(organismId)
        );
    }

    /**
     * 다른 유해 생물 정보 조회
     */
    private List<SimpleOrganismResponseDto> getOtherHarmfulOrganism(Long organismId) {
        List<HarmfulOrganism> harmfulOrganismList = harmfulOrganismRepository.findAllByOrganismIdNot(organismId);

        return convertHarmfulOrganismToSimple(harmfulOrganismList);
    }

    /**
     * BaseImageEntity 하위 클래스 리스트에서 url 추출
     */
    private List<String> extractImageUrl(List<? extends BaseImageEntity> imageList) {
        return imageList.stream()
                .map(item -> imageAssetUrlProcessor.prependCloudFrontHost(item.getUrl())).collect(Collectors.toList());
    }
}
