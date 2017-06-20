package com.testing.user;

import com.testing.model.User;
import com.testing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static java.util.Arrays.asList;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = userService.get(email).orElseThrow(() -> new RuntimeException("User not found."));

        return CustomUserDetails.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .entityId(user.getId())
                .slug(user.getSlug())
                .authorities(asList(new SimpleGrantedAuthority("test-role"), new SimpleGrantedAuthority("test-role2")))
                .scopes(asList("test-scope", "test-scope"))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }

}
