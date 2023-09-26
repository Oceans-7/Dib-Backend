package com.oceans7.dib.global.util;

import com.oceans7.dib.global.exception.ApplicationException;
import com.oceans7.dib.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {

    }

    public static final String ANONYMOUS_USER = "anonymousUser";

    public static Optional<Long> getCurrentUsername() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null || ANONYMOUS_USER.equals(authentication.getName())) {
            log.error("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        return Optional.ofNullable(Long.valueOf(authentication.getName()));

    }
}
