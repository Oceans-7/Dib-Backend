package com.oceans7.dib.domain.organism.entity;

import com.oceans7.dib.global.base_entity.BaseImageEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HarmfulOrganismImage extends BaseImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organism_image_id")
    private Long organismImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organism_id", nullable = false)
    private HarmfulOrganism harmfulOrganism;

    public static HarmfulOrganismImage of(String url) {
        HarmfulOrganismImage harmfulOrganismImage = new HarmfulOrganismImage();

        harmfulOrganismImage.setUrl(url);

        return harmfulOrganismImage;
    }

    public void setHarmfulOrganism(HarmfulOrganism harmfulOrganism) {
        this.harmfulOrganism = harmfulOrganism;
        harmfulOrganism.getHarmfulOrganismImageList().add(this);
    }
}
