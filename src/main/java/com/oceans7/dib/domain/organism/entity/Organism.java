package com.oceans7.dib.domain.organism.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Organism {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organism_id")
    protected Long organismId;

    @Column(name = "korean_name", length = 14, nullable = false)
    protected String koreanName;

    @Column(name = "english_name", length = 60, nullable = false)
    protected String englishName;

    @Column(name = "basic_appearance", nullable = false)
    protected String basicAppearance;

    @Column(name = "description", length = 100, nullable = false)
    protected String description;

    @Column(name = "detail_description", length = 500, nullable = false)
    protected String detailDescription;

    @Column(name = "first_image_url", length = 2100, nullable = false)
    protected String firstImageUrl;

    @Column(name = "illustration_image_url", length = 2100, nullable = false)
    protected String illustrationImageUrl;
}
