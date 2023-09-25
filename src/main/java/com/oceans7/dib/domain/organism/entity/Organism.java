package com.oceans7.dib.domain.organism.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Organism {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organism_id")
    private Long organismId;

    @Column(name = "korean_name")
    private String koreanName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "basic_appearance")
    private String basicAppearance;

    @Column(name = "description")
    private String description;

    @Column(name = "detail_description")
    private String detailDescription;

    @Column(name = "first_image_url")
    private String firstImageUrl;

    @Column(name = "illustration_image_url")
    private String illustrationImageUrl;
}
