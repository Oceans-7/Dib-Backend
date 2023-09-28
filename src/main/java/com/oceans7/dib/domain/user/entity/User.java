package com.oceans7.dib.domain.user.entity;

import com.oceans7.dib.domain.event.entity.Coupon;
import com.oceans7.dib.global.base_entity.BaseEntity;
import com.oceans7.dib.global.util.ValidatorUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Coupon> couponList = new ArrayList<>();

    public static User of(String profileUrl, String nickname, SocialType socialType, String socialUserId, Role role) {
        User user = new User();
        user.profileUrl = profileUrl;
        user.nickname = nickname;
        user.socialType = socialType;
        user.socialUserId = socialUserId;
        user.role = role;

        return user;
    }

    public void updateProfile(String nickname, String profileUrl) {
        this.nickname = ValidatorUtil.isNotEmpty(nickname) ? nickname : this.nickname;
        this.profileUrl = ValidatorUtil.isNotEmpty(profileUrl) ? profileUrl : this.profileUrl;
    }
}
