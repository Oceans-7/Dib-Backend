package com.oceans7.dib.domain.event.repository;

import com.oceans7.dib.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
