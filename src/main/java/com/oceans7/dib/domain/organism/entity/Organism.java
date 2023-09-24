package com.oceans7.dib.domain.organism.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@MappedSuperclass
public abstract class Organism {
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
}
