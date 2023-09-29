package com.oceans7.dib.global.base_entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
public abstract class BaseImageEntity {
    @Column(name = "url", length = 2100, nullable = false)
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }
}
