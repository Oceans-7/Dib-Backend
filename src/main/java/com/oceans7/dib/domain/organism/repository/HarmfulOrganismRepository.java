package com.oceans7.dib.domain.organism.repository;

import com.oceans7.dib.domain.organism.entity.HarmfulOrganism;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HarmfulOrganismRepository extends JpaRepository<HarmfulOrganism, Long> {
    List<HarmfulOrganism> findAllByOrganismIdNot(Long organismId);
}
