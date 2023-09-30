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

    public static <T extends Organism> T of(
            Class<T> clazz, String koreanName, String englishName,
            String basicAppearance, String description,
            String detailDescription, String firstImageUrl,
            String illustrationImageUrl) {
        try {
            T organism = clazz.getDeclaredConstructor().newInstance();
            organism.koreanName = koreanName;
            organism.englishName = englishName;
            organism.basicAppearance = basicAppearance;
            organism.description = description;
            organism.detailDescription = detailDescription;
            organism.firstImageUrl = firstImageUrl;
            organism.illustrationImageUrl = illustrationImageUrl;
            return organism;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create organism", e);
        }
    }
}
