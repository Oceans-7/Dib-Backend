package com.oceans7.dib.domain.user.entity;

import com.oceans7.dib.global.base_entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "social_type")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(name = "social_user_id")
    private String socialUserId;

    public static User of(String profileUrl, String nickname, SocialType socialType, String socialUserId) {
        User user = new User();
        user.profileUrl = profileUrl;
        user.nickname = nickname;
        user.socialType = socialType;
        user.socialUserId = socialUserId;

        return user;
    }
}
