package com.oceans7.dib.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final String[] permitUrls;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);

        Authentication authentication = new UsernamePasswordAuthenticationToken(token, "");

        Authentication authenticatedAuthentication = authenticationManager.authenticate(authentication);
        if (authenticatedAuthentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticatedAuthentication);
            filterChain.doFilter(request, response);
        } else {
            SecurityContextHolder.clearContext();
        }
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader =  request.getHeader("Authorization");
        return authorizationHeader.substring("Bearer ".length());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String path = request.getRequestURI();
        return isPermitUrl(path);
    }

    private boolean isPermitUrl(String path) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        return Arrays.stream(this.permitUrls).anyMatch(permitUrl -> antPathMatcher.match(permitUrl, path));
    }
}
