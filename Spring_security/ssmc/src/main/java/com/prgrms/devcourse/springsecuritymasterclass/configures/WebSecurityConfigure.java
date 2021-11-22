package com.prgrms.devcourse.springsecuritymasterclass.configures;

import com.prgrms.devcourse.springsecuritymasterclass.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

// ----------------------------------------------------------------------------------------------------------

//    // SecurityContextHolder의 스레드 로컬 변수 전략을 변경
//    public WebSecurityConfigure() {
//        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//    }

    @Bean
    @Qualifier("myAsyncTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("my-executor");
        return executor;
    }

    @Bean
    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(
            @Qualifier("myAsyncTaskExecutor") AsyncTaskExecutor delegate
    ) {
        return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
    }

    // 비밀번호 인코더 설정 (설정 안하면 prefix 붙여줘야됨)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

// ----------------------------------------------------------------------------------------------------------

//    // 인메모리 유저 등록
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("{noop}user123").roles("USER").and()
//                .withUser("admin01").password("{noop}admin123").roles("ADMIN").and()
//                .withUser("admin02").password("{noop}admin123").roles("ADMIN")
//        ;
//    }

// ----------------------------------------------------------------------------------------------------------

//    // UserDetailsService 커스텀 -> jdbcDao를 사용해 security-DB 연동
//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
//        jdbcDao.setDataSource(dataSource);
//        jdbcDao.setUsersByUsernameQuery(
//                "SELECT login_id, passwd, true FROM USERS WHERE login_id = ?"
//        );
//        jdbcDao.setGroupAuthoritiesByUsernameQuery(
//                "SELECT " +
//                    "u.login_id, g.name, p.name " +
//                "FROM " +
//                    "users u JOIN groups g ON u.group_id = g.id " +
//                    "LEFT JOIN group_permission gp ON g.id = gp.group_id " +
//                    "JOIN permissions p ON p.id = gp.permission_id " +
//                "WHERE " +
//                    "u.login_id = ?"
//        );
//        jdbcDao.setEnableAuthorities(false);
//        jdbcDao.setEnableGroups(true);
//        return jdbcDao;
//    }

// ----------------------------------------------------------------------------------------------------------

//    // AuthenticationManagerBuilder -> jdbcDao를 사용해 security-DB 연동
//    private DataSource dataSource;
//
//    @Autowired
//    public void setDataSource(DataSource dataSource){
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery(
//                        "SELECT login_id, passwd, true FROM USERS WHERE login_id = ?"
//                )
//                .groupAuthoritiesByUsername(
//                        "SELECT " +
//                                "u.login_id, g.name, p.name " +
//                        "FROM " +
//                                "users u JOIN groups g ON u.group_id = g.id " +
//                                "LEFT JOIN group_permission gp ON g.id = gp.group_id " +
//                                "JOIN permissions p ON p.id = gp.permission_id " +
//                        "WHERE " +
//                                "u.login_id = ?"
//                )
//                .getUserDetailsService()
//                .setEnableAuthorities(false)
//        ;
//    }

// ----------------------------------------------------------------------------------------------------------

    // Security - JPA 연동
    private final UserService userService;

//    @Autowired
//    public void setUserService(UserService userService){
//        this.userService = userService;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

// ----------------------------------------------------------------------------------------------------------

    // 전역설정 처리를 하는 API
    @Override
    public void configure(WebSecurity web) {
        // 보안설정 제외하기
        web.ignoring().antMatchers("/assets/**", "/h2-console/**");
    }

    // Spring security 설정하는 메소드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/me").hasAnyRole("USER", "ADMIN") // 인증영역 : me 요청시 "USER", "ADMIN"이어야 한다.
                    .antMatchers("/admin").access("hasRole('ADMIN') and isFullyAuthenticated()")    // 권한이 ADMIN + RememberMe로 접근하지 않아야 함
                    .anyRequest().permitAll()
                    .accessDecisionManager(accessDecisionManager())
//                    .expressionHandler(securityExpressionHandler())     // 커스텀 핸들러 지정 (권한에 대한)
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/")                             // 로그인 성공시 url
                    .permitAll()                                        // ???
                    .and()
                .httpBasic()
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

    // 커스텀 OddAdminVoter 추가하기 + UnanimousBased 전략으로 접근 제어
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> voters = new ArrayList<>();
        voters.add(new WebExpressionVoter());
        voters.add(new OddAdminVoter(new AntPathRequestMatcher("/admin")));
        return new UnanimousBased(voters);
    }

}