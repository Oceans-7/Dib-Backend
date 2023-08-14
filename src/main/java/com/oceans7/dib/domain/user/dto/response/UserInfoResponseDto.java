package com.oceans7.dib.domain.user.dto.response;

import com.oceans7.dib.domain.auth.user_account.UserAccount;
import com.oceans7.dib.domain.user.entity.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@NoArgsConstructor()
public class UserInfoResponseDto {

    private Long userId;

    private String profileUrl;

    private String nickName;

    private Collection<GrantedAuthority> role;

    public static UserInfoResponseDto of(Long userId, String profileUrl, String nickName, Collection<GrantedAuthority> role) {
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();
        userInfoResponseDto.userId = userId;
        userInfoResponseDto.profileUrl = profileUrl;
        userInfoResponseDto.nickName = nickName;
        userInfoResponseDto.role = role;
        return userInfoResponseDto;
    }

    public static UserInfoResponseDto from(UserAccount userAccount) {
        UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto();
        userInfoResponseDto.userId = userAccount.getUserId();
        userInfoResponseDto.profileUrl = userAccount.getProfileUrl();
        userInfoResponseDto.nickName = userAccount.getNickName();
        userInfoResponseDto.role = userAccount.getAuthorities();
        return userInfoResponseDto;
    }
}
