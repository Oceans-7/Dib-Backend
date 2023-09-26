package com.oceans7.dib.domain.report.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ReportRequestDto {
    @Schema(description = "유해 생물 이름", example = "갯주풀 / 영국 갯끈풀")
    private String organismName;

    @Schema(description = "발견 위치", example = "강원 강릉시 강문동")
    private String foundLocation;

    @ArraySchema(schema = @Schema(description = "이미지 URL", implementation = String.class))
    private List<String> imageUrlList;
}
