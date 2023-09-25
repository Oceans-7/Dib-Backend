package com.oceans7.dib.global.base_entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
public abstract class BaseImageEntity {
    @Column(name = "url", nullable = false)
    private String url;
}
