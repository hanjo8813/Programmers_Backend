package com.prgrms.devcourse.springsecuritymasterclass.jwt;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private String credentials;

    // 인증되지 않은 토큰 생성
   public JwtAuthenticationToken(String principal, String credentials) {
        super(null);    // 인증전 권한 목록은 null
        super.setAuthenticated(false);  // 아직 인증 되지 않았음

        this.principal = principal;
        this.credentials = credentials;
    }

    // 인증된 토큰 생성
    JwtAuthenticationToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
       // true flag 변경은 생성자를 통해서만 할 수 있도록~
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("principal", principal)
                .append("credentials", "[PROTECTED]")
                .toString();
    }

}
