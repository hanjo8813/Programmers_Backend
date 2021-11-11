package com.prgrms.devcourse.springsecuritymasterclass.configures;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    // 전역설정 처리를 하는 API
    @Override
    public void configure(WebSecurity web) {
        // 보안설정 제외하기
        web.ignoring().antMatchers("/assets/**");
    }

    // Spring security 설정하는 메소드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    // 인증영역 : me 요청시 "USER", "ADMIN"이어야 한다.
                    // 인가없이 /me에 접근시 /login 페이지로 redirect
                    .antMatchers("/me").hasAnyRole("USER", "ADMIN")
                    // 익명영역 : 인가없이 /me에
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/")
                    .permitAll()
                    .and()
        ;
    }
}