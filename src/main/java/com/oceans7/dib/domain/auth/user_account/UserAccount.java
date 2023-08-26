package com.oceans7.dib.domain.auth.user_account;

import com.oceans7.dib.domain.user.entity.Role;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {

    private Long userId;

    private String profileUrl;

    private String nickName;

    public UserAccount(Long userId, String profileUrl, String nickName, Role role) {

        super(userId.toString(), "", new ArrayList<Role>() {{
            add(role);
        }});

        this.userId = userId;
        this.profileUrl = profileUrl;
        this.nickName = nickName;

    }
}
