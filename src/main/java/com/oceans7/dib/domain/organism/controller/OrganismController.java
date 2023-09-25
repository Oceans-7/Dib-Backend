package com.oceans7.dib.domain.organism.controller;

import com.oceans7.dib.domain.organism.dto.response.OrganismResponseDto;
import com.oceans7.dib.domain.organism.dto.response.SimpleOrganismResponseDto;
import com.oceans7.dib.domain.organism.service.OrganismService;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "organism(marine/harmful)", description = "해양/유해 생물 관련 API")
@RestController
@RequiredArgsConstructor
public class OrganismController {
    private final OrganismService organismService;

    @Operation(
            summary = "해양 생물 전체 조회",
            description = "해양 생물의 전체 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/marine-organism")
    public ApplicationResponse<List<SimpleOrganismResponseDto>> getAllMarineOrganism() {
        return ApplicationResponse.ok(organismService.getAllMarineOrganism());
    }

    @Operation(
            summary = "해양 생물 상세 조회",
            description = "해양 생물의 상세 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/marine-organism/{organismId}")
    public ApplicationResponse<OrganismResponseDto> getMarineOrganismDetail(@PathVariable(name = "organismId")Long organismId) {
        return ApplicationResponse.ok(organismService.getMarineOrganismDetail(organismId));
    }

    @Operation(
            summary = "유해 생물 전체 조회",
            description = "유해 생물의 전체 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/harmful-organism")
    public ApplicationResponse<List<SimpleOrganismResponseDto>> getAllHarmfulOrganism() {
        return ApplicationResponse.ok(organismService.getAllHarmfulOrganism());
    }

    @Operation(
            summary = "유해 생물 상세 조회",
            description = "유해 생물의 상세 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @GetMapping("/harmful-organism/{organismId}")
    public ApplicationResponse<OrganismResponseDto> getMarineHarmfulDetail(@PathVariable(name = "organismId")Long organismId) {
        return ApplicationResponse.ok(organismService.getHarmfulOrganismDetail(organismId));
    }
}
