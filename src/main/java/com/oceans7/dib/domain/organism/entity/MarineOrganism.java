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
public class MarineOrganism extends Organism{
    @OneToMany(mappedBy = "marineOrganism")
    private List<MarineOrganismImage> marineOrganismImageList = new ArrayList<>();

    public static MarineOrganism of(String koreanName, String englishName, String basicAppearance, String description, String detailDescription,
                                     String firstImageUrl, String illustrationImageUrl) {
        MarineOrganism organism = new MarineOrganism();

        organism.koreanName = koreanName;
        organism.englishName = englishName;
        organism.basicAppearance = basicAppearance;
        organism.description = description;
        organism.detailDescription = detailDescription;
        organism.firstImageUrl = firstImageUrl;
        organism.illustrationImageUrl = illustrationImageUrl;

        return organism;
    }
}
