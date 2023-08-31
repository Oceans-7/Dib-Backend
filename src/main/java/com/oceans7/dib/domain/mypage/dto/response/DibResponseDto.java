package com.oceans7.dib.domain.mypage.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DibResponseDto {

    @Schema(description = "찜 목록 개수", example = "5")
    private int count;

    @ArraySchema(schema = @Schema(description = "찜 정보", implementation = DetailDibResponseDto.class))
    List<DetailDibResponseDto> dibList;

    public static DibResponseDto of(List<DetailDibResponseDto> dibList) {
        DibResponseDto dibResponseDto = new DibResponseDto();

        dibResponseDto.count = dibList.size();
        dibResponseDto.dibList = dibList;

        return dibResponseDto;
    }
}
