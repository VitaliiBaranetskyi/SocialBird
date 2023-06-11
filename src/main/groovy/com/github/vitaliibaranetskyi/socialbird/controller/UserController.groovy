package com.github.vitaliibaranetskyi.socialbird.controller

import com.github.vitaliibaranetskyi.socialbird.dto.UserDTO
import com.github.vitaliibaranetskyi.socialbird.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @GetMapping("/all")
    ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers()
        ResponseEntity.ok(users)
    }

    @PostMapping("/register")
    ResponseEntity<UserDTO> createUser(@RequestParam String username, @RequestParam String password) {
        UserDTO createdUser = userService.createUser(username, password)
        ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PutMapping("/{id}")
    ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestParam String username, @RequestParam String password) {
        UserDTO updatedUser = userService.updateUser(id, username, password)
        ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id)
        ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        UserDTO user = userService.getUserById(id)
        ResponseEntity.ok(user)
    }

    @GetMapping
    ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO user = userService.getUserByUsername(username)
        ResponseEntity.ok(user)
    }

}
