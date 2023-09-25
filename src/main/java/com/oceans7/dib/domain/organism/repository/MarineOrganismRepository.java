package com.oceans7.dib.domain.organism.repository;

import com.oceans7.dib.domain.organism.entity.MarineOrganism;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarineOrganismRepository extends JpaRepository<MarineOrganism, Long> {
    List<MarineOrganism> findAllByOrganismIdNot(Long organismId);
}
