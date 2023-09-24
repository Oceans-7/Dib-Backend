package com.oceans7.dib.global.base_entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BaseImage {
    private String url;
}
