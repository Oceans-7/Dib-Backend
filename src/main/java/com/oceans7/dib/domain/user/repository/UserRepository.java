package com.oceans7.dib.domain.user.repository;


import com.oceans7.dib.domain.user.entity.SocialType;
import com.oceans7.dib.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialTypeAndSocialUserId(SocialType socialType, String socialUserId);

    Optional<User> findByNickname(String nickname);
}
