package com.oceans7.dib.domain.organism.repository;

import com.oceans7.dib.domain.organism.entity.HarmfulOrganismImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarmfulOrganismImageRepository extends JpaRepository<HarmfulOrganismImage, Long> {
}
