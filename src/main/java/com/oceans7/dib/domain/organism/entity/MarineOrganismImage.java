package com.oceans7.dib.domain.organism.entity;

import com.oceans7.dib.global.base_entity.BaseImageEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarineOrganismImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organism_image_id")
    private Long organismImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organism_id", nullable = false)
    private MarineOrganism marineOrganism;

    public static MarineOrganismImage of(String url) {
        MarineOrganismImage marineOrganismImage = new MarineOrganismImage();

        marineOrganismImage.setUrl(url);

        return marineOrganismImage;
    }

    public void setMarineOrganism(MarineOrganism marineOrganism) {
        this.marineOrganism = marineOrganism;
        marineOrganism.getMarineOrganismImageList().add(this);
    }
}
