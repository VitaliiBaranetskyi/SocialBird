package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.model.User
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository

    UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null)
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username)
        }
        new org.springframework.security.core.userdetails.User(
                user.username,
                user.password,
                Collections.emptyList()
        )
    }
}
