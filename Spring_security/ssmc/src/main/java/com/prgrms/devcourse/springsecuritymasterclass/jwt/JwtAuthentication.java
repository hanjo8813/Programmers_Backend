package com.prgrms.devcourse.springsecuritymasterclass.jwt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

// User 인스턴스를 대체하는 객체가 될것임
public class JwtAuthentication {

    public final String token;
    public final String username;

    JwtAuthentication(String token, String username) {
        checkArgument(isNotEmpty(token), "token must be provided.");
        checkArgument(isNotEmpty(username), "username must be provided.");

        this.token = token;
        this.username = username;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("token", token)
                .append("username", username)
                .toString();
    }

}