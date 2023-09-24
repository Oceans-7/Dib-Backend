package com.oceans7.dib.domain.organism.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarineOrganism extends Organism{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organism_id")
    private Long organismId;

}
