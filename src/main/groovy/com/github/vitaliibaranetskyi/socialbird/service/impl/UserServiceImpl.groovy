package com.github.vitaliibaranetskyi.socialbird.service.impl


import com.github.vitaliibaranetskyi.socialbird.dto.UserDTO
import com.github.vitaliibaranetskyi.socialbird.exception.EmptyPasswordException
import com.github.vitaliibaranetskyi.socialbird.exception.EmptyUsernameException
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.exception.UsernameAlreadyTakenException
import com.github.vitaliibaranetskyi.socialbird.mapper.UserMapper
import com.github.vitaliibaranetskyi.socialbird.model.User
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository
    private final UserMapper userMapper
    private final PasswordEncoder passwordEncoder

    UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository
        this.userMapper = userMapper
        this.passwordEncoder = passwordEncoder
    }

    List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll()
        userMapper.toDtoList(users)
    }

    UserDTO createUser(String username, String password) {
        if (username.isBlank()) {
            throw new EmptyUsernameException("Username cannot be empty")
        }

        if (password.isBlank()) {
            throw new EmptyPasswordException("Password cannot be empty")
        }

        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyTakenException("Username is already taken")
        }

        User user = new User(
                username: username,
                password: passwordEncoder.encode(password)
        )
        User savedUser = userRepository.save(user)
        userMapper.toDto(savedUser)
    }

    UserDTO updateUser(String id, String username, String password) {
        User user = userRepository.findById(id).orElse(null)
        if (user) {
            if (username.isBlank()) {
                throw new EmptyUsernameException("Username cannot be empty")
            }

            if (password.isBlank()) {
                throw new EmptyPasswordException("Password cannot be empty")
            }

            if (user.getUsername() != username && userRepository.existsByUsername(username)) {
                throw new UsernameAlreadyTakenException("Username is already taken")
            }

            user.username = username
            user.password = passwordEncoder.encode(password)
            User updatedUser = userRepository.save(user)
            userMapper.toDto(updatedUser)
        } else {
            throw new EntityNotFoundException("User not found")
        }
    }

    void deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null)
        if (!user) {
            throw new EntityNotFoundException("User not found")
        }

        userRepository.deleteById(id)
    }

    UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElse(null)
        if (!user) {
            throw new EntityNotFoundException("User not found")
        }

        userMapper.toDto(user)
    }

    UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null)
        if (!user) {
            throw new EntityNotFoundException("User not found")
        }

        userMapper.toDto(user)
    }
}
