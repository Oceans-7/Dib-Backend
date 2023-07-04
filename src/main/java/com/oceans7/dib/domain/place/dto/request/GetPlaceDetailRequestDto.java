package com.oceans7.dib.domain.place.dto.request;

import com.oceans7.dib.domain.place.dto.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPlaceDetailRequestDto {

    @Schema(description = "컨텐츠 아이디(필수), 관광 리스트 조회시 확인 가능", example = "126508")
    private Long contentId;

    @Schema(description = "컨텐츠 타입(필수), 관광 정보 리스트 시 확인 가능", example = "TOURIST_SPOT")
    private ContentType contentType;

}
