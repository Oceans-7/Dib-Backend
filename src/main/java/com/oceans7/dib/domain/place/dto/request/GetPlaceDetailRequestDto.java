package com.oceans7.dib.domain.place.dto.request;

import com.oceans7.dib.domain.place.dto.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetPlaceDetailRequestDto {

    @NotNull
    @Schema(description = "컨텐츠 아이디 (관광 리스트 조회시 확인 가능)", example = "126508")
    private Long contentId;

    @NotEmpty
    @Schema(description = "컨텐츠 타입 (관광 리스트 조회시 확인 가능)", example = "TOURIST_SPOT")
    private ContentType contentType;

}
