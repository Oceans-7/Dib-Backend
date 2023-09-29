package com.oceans7.dib.domain.custom_content.repository;

import com.oceans7.dib.domain.custom_content.entity.CustomContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomContentRepository extends JpaRepository<CustomContent, Long> {
}
