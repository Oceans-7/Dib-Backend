package com.oceans7.dib.place.dto.request;

import com.oceans7.dib.place.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPlaceDetailRequestDto {

    @Schema(description = "컨텐츠 아이디, 위치 기반 조회시 확인 가능", example = "126508")
    private String contentId;

    @Schema(description = "컨텐츠 타입", example = "TOURIST_SPOT")
    private ContentType contentType;

}
