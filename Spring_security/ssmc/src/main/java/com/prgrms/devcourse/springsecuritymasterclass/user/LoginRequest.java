package com.prgrms.devcourse.springsecuritymasterclass.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class LoginRequest {
    private String principal;
    private String credentials;
}
