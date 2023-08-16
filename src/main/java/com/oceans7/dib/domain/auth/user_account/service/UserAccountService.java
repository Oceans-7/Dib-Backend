package com.oceans7.dib.domain.auth.user_account.service;

import com.oceans7.dib.domain.auth.user_account.UserAccount;
import com.oceans7.dib.domain.user.entity.Role;
import com.oceans7.dib.global.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService implements UserDetailsService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        Long userId = claims.getBody().get("user_id", Long.class);
        String profileUrl = claims.getBody().get("profile_url", String.class);
        String nickName = claims.getBody().get("nick_name", String.class);
        Role role = Role.valueOf(claims.getBody().get("role", String.class));

        return new UserAccount(userId, profileUrl, nickName, role);
    }
}
