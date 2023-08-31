package com.oceans7.dib.domain.user_refresh_token.repository;

import com.oceans7.dib.domain.user_refresh_token.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);
}
