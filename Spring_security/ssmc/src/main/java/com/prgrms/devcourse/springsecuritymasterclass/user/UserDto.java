package com.prgrms.devcourse.springsecuritymasterclass.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class UserDto {

    private String token;
    private String username;
    private String group;

}
