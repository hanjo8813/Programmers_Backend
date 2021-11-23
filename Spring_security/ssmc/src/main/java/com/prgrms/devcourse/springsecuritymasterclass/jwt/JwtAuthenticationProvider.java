package com.prgrms.devcourse.springsecuritymasterclass.jwt;

import com.prgrms.devcourse.springsecuritymasterclass.user.User;
import com.prgrms.devcourse.springsecuritymasterclass.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

import static org.apache.commons.lang3.ClassUtils.isAssignable;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;
    private final UserService userService;

    @Override
    public boolean supports(Class<?> authentication) {
        return isAssignable(JwtAuthenticationToken.class, authentication);
    }

    // 인증 전 JwtAuthenticationToken 토큰을 가지고 Authentication 객체 생성
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
        return processUserAuthentication(
                String.valueOf(jwtAuthentication.getPrincipal()),
                jwtAuthentication.getCredentials()
        );
    }

    private Authentication processUserAuthentication(String principal, String credentials) {
        try {
            // login 메소드가 필터 계층에서 사용된다..!? -> 권한 목록을 가져옴
            User user = userService.login(principal, credentials);
            List<GrantedAuthority> authorities = user.getGroup().getAuthorities();
            // 서명된 jwt 토큰을 생성
            String token = getToken(user.getLoginId(), authorities);
            // AuthToken을 생성
            JwtAuthenticationToken authenticated =
                    new JwtAuthenticationToken(new JwtAuthentication(token, user.getLoginId()), null, authorities);
            authenticated.setDetails(user);
            return authenticated;
        } catch (IllegalArgumentException e) {
            // 비번 틀린 경우
            throw new BadCredentialsException(e.getMessage());
        } catch (DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    private String getToken(String username, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
        return jwt.sign(Jwt.Claims.from(username, roles));
    }

}