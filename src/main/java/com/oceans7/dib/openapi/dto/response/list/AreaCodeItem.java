package com.oceans7.dib.openapi.dto.response.list;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AreaCodeItem {
    private String name;
    private String code;
}
