package com.oceans7.dib.domain.organism.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HarmfulOrganism extends Organism {
    @OneToMany(mappedBy = "harmfulOrganism")
    private List<HarmfulOrganismImage> harmfulOrganismImageList = new ArrayList<>();

    @Column(name = "report_number")
    private String reportNumber;

    public static HarmfulOrganism of(String koreanName, String englishName, String basicAppearance, String description, String detailDescription,
                                     String firstImageUrl, String illustrationImageUrl, String reportNumber) {
        HarmfulOrganism organism = new HarmfulOrganism();

        organism.koreanName = koreanName;
        organism.englishName = englishName;
        organism.basicAppearance = basicAppearance;
        organism.description = description;
        organism.detailDescription = detailDescription;
        organism.firstImageUrl = firstImageUrl;
        organism.illustrationImageUrl = illustrationImageUrl;
        organism.reportNumber = reportNumber;

        return organism;
    }
}
