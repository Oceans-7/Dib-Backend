package com.oceans7.dib.domain.place.repository;

import com.oceans7.dib.domain.place.entity.Dib;
import com.oceans7.dib.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DibRepository extends JpaRepository<Dib, Long> {
    boolean existsByUserAndContentId(User user, Long contentId);

    void deleteByUserAndContentId(User user, Long contentId);

    Long countByUser(User user);

    List<Dib> findByUser(User user);
}
