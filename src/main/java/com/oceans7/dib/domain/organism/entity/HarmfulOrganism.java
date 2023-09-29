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
}
