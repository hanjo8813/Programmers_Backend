package com.prgrms.devcourse.springsecuritymasterclass.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginId(username)
                .map(user ->
                        // security - User 임
                        User.builder()
                                .username(user.getLoginId())
                                .password(user.getPasswd())
                                .authorities(user.getGroup().getAuthorities())
                                .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + username));
    }

}