package com.prgrms.devcourse.springsecuritymasterclass.user;

import com.prgrms.devcourse.springsecuritymasterclass.jwt.Jwt;
import com.prgrms.devcourse.springsecuritymasterclass.jwt.JwtAuthentication;
import com.prgrms.devcourse.springsecuritymasterclass.jwt.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserRestController {

//    private final Jwt jwt;

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    /**
     * 보호받는 엔드포인트 - ROLE_USER 또는 ROLE_ADMIN 권한 필요함
     */
    @GetMapping(path = "/user/me")
    // 헤더에 담긴 jwt base64는 Filter를 통해 디코딩 -> 토큰 생성되고...
    // AuthenticationPrincipalArgumentResolver 를 통해 JwtAuthentication 객체를 리턴받을 수 있음
    public UserDto me(@AuthenticationPrincipal JwtAuthentication authentication) {
        return userService.findByLoginId(authentication.username)
                .map(user ->
                        new UserDto(authentication.token, authentication.username, user.getGroup().getName())
                )
                .orElseThrow(() -> new IllegalArgumentException("Could not found user for " + authentication.username));
    }

    /**
     * 사용자 로그인
     */
    @PostMapping(path = "/user/login")
    public UserDto login(@RequestBody LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getPrincipal(), request.getCredentials());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) resultToken;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        User user = (User) resultToken.getDetails();
        return new UserDto(principal.token, principal.username, user.getGroup().getName());
    }



//
//    /**
//     * 보호받는 엔드포인트 - ROLE_USER 또는 ROLE_ADMIN 권한 필요함
//     * @return 사용자명
//     */
//    @GetMapping(path = "/user/me")
//    public String me() {
//        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
//
//    /**
//     * 주어진 사용자의 JWT 토큰을 출력함
//     * @param username 사용자명
//     * @return JWT 토큰
//     */
//    @GetMapping(path = "/user/{username}/token")
//    public String getToken(@PathVariable String username) {
//        UserDetails userDetails = userService.loadUserByUsername(username);
//        String[] roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .toArray(String[]::new);
//        return jwt.sign(Jwt.Claims.from(userDetails.getUsername(), roles));
//    }
//
//    /**
//     * 주어진 JWT 토큰 디코딩 결과를 출력함
//     * @param token JWT 토큰
//     * @return JWT 디코드 결과
//     */
//    @GetMapping(path = "/user/token/verify")
//    public Map<String, Object> verify(@RequestHeader("token") String token) {
//        return jwt.verify(token).asMap();
//    }

}