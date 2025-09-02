package com.hex.customerSupportApp.service;

import com.hex.customerSupportApp.entity.User;
import com.hex.customerSupportApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        var authority = "ROLE_" + user.getRole();
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authority)
                .build();
    }

}
