package com.prgrms.devcourse.springsecuritymasterclass.configures;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        Map encoders = new HashMap();
//        encoders.put("noop", NoOpPasswordEncoder.getInstance());
//        PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("noop", encoders);
//        auth.inMemoryAuthentication()
//                .withUser("user").password(passwordEncoder.encode("user123")).roles("USER").and()
//                .withUser("admin").password(passwordEncoder.encode("admin123")).roles("ADMIN");
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user123").roles("USER").and()
                .withUser("admin01").password("{noop}admin123").roles("ADMIN").and()
                .withUser("admin02").password("{noop}admin123").roles("ADMIN")
        ;
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
                    .antMatchers("/me").hasAnyRole("USER", "ADMIN") // 인증영역 : me 요청시 "USER", "ADMIN"이어야 한다.
                    .antMatchers("/admin").access("hasRole('ADMIN') and isFullyAuthenticated() and oddAdmin")    // 권한이 ADMIN + RememberMe로 접근하지 않아야 함
                    .anyRequest().permitAll()
                    .expressionHandler(securityExpressionHandler())     // 커스텀 핸들러 지정 (권한에 대한)
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/")                             // 로그인 성공시 url
                    .permitAll()                                        // ???
                    .and()
                .rememberMe()                                           // 쿠키를 통한 자동 로그인
                    .key("key")                                            //
                    .tokenValiditySeconds(60*5)                         // expire 기간 지정
                    .rememberMeParameter("param")                       // 화면 체크박스의 'name' 속성을 지정
                    .rememberMeCookieName("cookie_name")                //
                    .and()
                .logout()
                    .logoutUrl("/logout")                               // 로그아웃 요청 url (default)
                    .logoutSuccessUrl("/login")                         // 로그아웃 성공시 리다이렉트 url
                    .invalidateHttpSession(true)                        // 로그아웃시 세션 제거하기 (default)
                    .clearAuthentication(true)                          // security context 비우기 (default)
                    .and()
                .anonymous()
                    .principal("익명유저")                                // 익명유저 name 지정
                    .authorities("ROLE_ANONYMOUS", "ROLE_UNKNOWN")      // 익명유저 권한 지정
                    .and()
                .sessionManagement()
                    .invalidSessionUrl("/")
                    .sessionFixation().changeSessionId()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .and()
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(customAccessDeniedHandler())

        ;
    }

    // AccessDeniedHandler 커스텀
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, e) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication != null ? authentication.getPrincipal() : null ;
            log.warn("{} 거절됨", principal, e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain");
            response.getWriter().write("## ACCESS DENIED ##");
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

    // 커스텀한 SecurityExpressionHandler 팩토리 메소드
    public SecurityExpressionHandler<FilterInvocation> securityExpressionHandler() {
        return new CustomWebSecurityExpressionHandler(
                new AuthenticationTrustResolverImpl(),
                "ROLE_"
        );
    }

}