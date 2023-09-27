package com.oceans7.dib.global.api.http.KhoaDataType;

import lombok.Getter;

@Getter
public enum KhoaDataType {
    tideObsTemp("tideObsTemp"),
    tideObsPreTab("tideObsPreTab"),
    obsWaveHight("obsWaveHight"),
    fcIndexOfType("fcIndexOfType"),
    ;

    private String value;

    KhoaDataType(String value) {
        this.value = value;
    }
}
