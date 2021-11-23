package com.prgrms.devcourse.springsecuritymasterclass.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
@Service
//public class UserService implements UserDetailsService {
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User login(String principal, String credentials) {
        checkArgument(isNotEmpty(principal), "principal must be provided.");
        checkArgument(isNotEmpty(credentials), "credentials must be provided.");
        User user = userRepository.findByLoginId(principal)
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLoginId(String loginId) {
        checkArgument(isNotEmpty(loginId), "loginId must be provided.");
        return  userRepository.findByLoginId(loginId);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByLoginId(username)
//                .map(user ->
//                        // security - User 임
//                        User.builder()
//                                .username(user.getLoginId())
//                                .password(user.getPasswd())
//                                .authorities(user.getGroup().getAuthorities())
//                                .build()
//                )
//                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + username));
//    }

}