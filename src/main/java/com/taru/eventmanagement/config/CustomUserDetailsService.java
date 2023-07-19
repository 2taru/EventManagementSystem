package com.taru.eventmanagement.config;

import com.taru.eventmanagement.models.Role;
import com.taru.eventmanagement.models.User;
import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.repositories.UserRepository;
import com.taru.eventmanagement.repositories.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new /*UserNotFoundException*/RuntimeException("User with username = " + username + " - not found!"));
        UserRole userRole = userRoleRepository.findByUserUserId(user.getUserId())
                .orElseThrow(() -> new /*RoleNotFoundException*/RuntimeException("User with id = " + user.getUserId() + " - don't have a role!"));
        List<Role> roles = Collections.singletonList(userRole.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRoleToGACollection(roles)
        );
    }

    private Collection<GrantedAuthority> mapRoleToGACollection(List<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }
}
