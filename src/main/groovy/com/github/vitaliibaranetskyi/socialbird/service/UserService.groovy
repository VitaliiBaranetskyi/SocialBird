package com.github.vitaliibaranetskyi.socialbird.service

import com.github.vitaliibaranetskyi.socialbird.dto.UserDTO
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService{
    List<UserDTO> getAllUsers()
    UserDTO createUser(String username, String password)
    UserDTO updateUser(String id, String username, String password)
    void deleteUser(String id)
    UserDTO getUserById(String id)
    UserDTO getUserByUsername(String username)
}
