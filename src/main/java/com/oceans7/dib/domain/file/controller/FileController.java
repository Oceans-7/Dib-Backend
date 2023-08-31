package com.oceans7.dib.domain.file.controller;

import com.oceans7.dib.domain.file.dto.FileCategory;
import com.oceans7.dib.domain.file.dto.request.ImageNameRequestDto;
import com.oceans7.dib.domain.file.service.FileService;
import com.oceans7.dib.global.response.ApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "file", description = "파일 업로드 API")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(
            summary = "s3 presigned url 반환",
            description = "유저가 s3에 업로드할 수 있는 presigned url을 반환한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @PostMapping("/{category}/presigned-url")
    public ApplicationResponse<String> createPresignedUrl(
            @PathVariable("category")FileCategory category, @RequestBody ImageNameRequestDto imageNameRequestDto) {
        return ApplicationResponse.ok(fileService.getPreSignedUrl(category.getPrefix(), imageNameRequestDto.getImageName()));
    }
}
