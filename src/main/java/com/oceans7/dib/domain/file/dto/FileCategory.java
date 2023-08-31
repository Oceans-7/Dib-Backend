package com.oceans7.dib.domain.file.dto;

import lombok.Getter;

@Getter
public enum FileCategory {
    IMAGE("images"),
    ;

    private String prefix;

    FileCategory(String prefix) {
        this.prefix = prefix;
    }
}
