package com.oceans7.dib.domain.mypage.dto.response;

import com.oceans7.dib.global.util.ValidatorUtil;
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

    public static DibResponseDto from(List<DetailDibResponseDto> dibList) {
        DibResponseDto dibResponseDto = new DibResponseDto();

        dibResponseDto.count = ValidatorUtil.isEmpty(dibList) ? 0 : dibList.size();
        dibResponseDto.dibList = ValidatorUtil.isEmpty(dibList) ? null : dibList;

        return dibResponseDto;
    }
}
