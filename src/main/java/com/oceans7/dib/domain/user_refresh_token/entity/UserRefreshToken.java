package com.oceans7.dib.domain.user_refresh_token.entity;

import com.oceans7.dib.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token", columnDefinition = "VARCHAR(500)")
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static UserRefreshToken of(String refreshToken, User user) {
        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.refreshToken = refreshToken;
        userRefreshToken.user = user;

        return userRefreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
