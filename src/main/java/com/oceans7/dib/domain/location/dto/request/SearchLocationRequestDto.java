package com.oceans7.dib.domain.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchLocationRequestDto {

    @NotNull
    @Schema(description = "경도 (longitude)", example = "127.637058787484")
    private double mapX;

    @NotNull
    @Schema(description = "위도 (latitude)", example = "37.2984233734535")
    private double mapY;
}
