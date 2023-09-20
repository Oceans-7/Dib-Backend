package com.oceans7.dib.global.api.response.kakao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalResponse {
    @JsonProperty("documents")
    private List<AddressItem> addressItems;

    @JsonCreator
    public LocalResponse(@JsonProperty("documents") List<AddressItem> addressItems) {
        this.addressItems = addressItems;
    }

}
