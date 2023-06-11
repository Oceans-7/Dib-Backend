package com.oceans7.dib.location.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchLocationRequestDto {

    @NotNull
    @Schema(description = "주소(키워드 검색 가능하긴한데 일단 주소 검색으로? 관련 스레드:https://www.notion.so/246dceddfa394e7da6396c6e313fe638?d=d9c85f8d8bb24dc895d64a4c66a83d32&pvs=4#60ac77c02710439489cc7131b1ec9091) ", example = "36.000000")
    private String address;
}
