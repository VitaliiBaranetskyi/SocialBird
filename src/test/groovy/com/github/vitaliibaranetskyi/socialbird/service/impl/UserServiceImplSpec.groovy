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
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    UserRepository userRepository
    UserMapper userMapper
    PasswordEncoder passwordEncoder
    UserService userService

    def setup() {
        userRepository = Mock()
        userMapper = Mock()
        passwordEncoder = Mock()
        userService = new UserServiceImpl(userRepository, userMapper, passwordEncoder)
    }

    def "getAllUsers should return list of UserDTOs"() {
        given:
        List<User> users = [new User(id: "1", username: "user1"), new User(id: "2", username: "user2")]
        List<UserDTO> expectedUserDTOs = [new UserDTO(id: "1", username: "user1"), new UserDTO(id: "2", username: "user2")]

        when:
        List<UserDTO> result = userService.getAllUsers()

        then:
        1 * userRepository.findAll() >> users
        1 * userMapper.toDtoList(users) >> expectedUserDTOs
        result == expectedUserDTOs
    }

    def "createUser should create a new user and return UserDTO"() {
        given:
        def username = "testuser"
        def password = "testpassword"
        def encodedPassword = "encodedpassword"
        def savedUser = new User(id: "1", username: username, password: encodedPassword)
        def expectedUserDTO = new UserDTO(id: "1", username: username)

        when:
        UserDTO result = userService.createUser(username, password)

        then:
        1 * userRepository.existsByUsername(username) >> false
        1 * passwordEncoder.encode(password) >> encodedPassword
        1 * userRepository.save(_ as User) >> savedUser
        1 * userMapper.toDto(savedUser) >> expectedUserDTO
        result == expectedUserDTO
    }

    def "createUser should throw EmptyUsernameException when username is blank"() {
        given:
        def username = ""
        def password = "testpassword"

        when:
        userService.createUser(username, password)

        then:
        def exception = thrown(EmptyUsernameException)
        exception.message == "Username cannot be empty"
    }


    def "createUser should throw EmptyPasswordException when password is blank"() {
        given:
        def username = "testuser"
        def password = ""

        when:
        userService.createUser(username, password)

        then:
        def exception = thrown(EmptyPasswordException)
        exception.message == "Password cannot be empty"
    }

    def "createUser should throw UsernameAlreadyTakenException when username is already taken"() {
        given:
        def username = "existinguser"
        def password = "testpassword"
        userRepository.existsByUsername(username) >> true

        when:
        userService.createUser(username, password)

        then:
        def exception = thrown(UsernameAlreadyTakenException)
        exception.message == "Username is already taken"
    }

    def "updateUser should update an existing user and return the updated UserDTO"() {
        given:
        def id = "1"
        def username = "newusername"
        def password = "newpassword"
        def encodedPassword = "encodedpassword"
        def existingUser = new User(id: id, username: "oldusername", password: "oldpassword")
        def updatedUser = new User(id: id, username: username, password: encodedPassword)
        def expectedUserDTO = new UserDTO(id: id, username: username)

        when:
        UserDTO result = userService.updateUser(id, username, password)

        then:
        1 * userRepository.findById(id) >> Optional.of(existingUser)
        1 * userRepository.existsByUsername(username) >> false
        1 * passwordEncoder.encode(password) >> encodedPassword
        1 * userRepository.save(existingUser) >> updatedUser
        1 * userMapper.toDto(updatedUser) >> expectedUserDTO
        result == expectedUserDTO
    }

    def "updateUser should throw EntityNotFoundException when user with the given id does not exist"() {
        given:
        def id = "1"
        def username = "newusername"
        def password = "newpassword"
        userRepository.findById(id) >> Optional.empty()

        when:
        userService.updateUser(id, username, password)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "User not found"
    }

    def "updateUser should throw EmptyUsernameException when username is blank"() {
        given:
        def id = "1"
        def username = ""
        def password = "newpassword"
        def existingUser = new User(id: id, username: "oldusername", password: "oldpassword")
        userRepository.findById(id) >> Optional.of(existingUser)

        when:
        userService.updateUser(id, username, password)

        then:
        def exception = thrown(EmptyUsernameException)
        exception.message == "Username cannot be empty"
    }

    def "updateUser should throw EmptyPasswordException when password is blank"() {
        given:
        def id = "1"
        def username = "newusername"
        def password = ""
        def existingUser = new User(id: id, username: "oldusername", password: "oldpassword")
        userRepository.findById(id) >> Optional.of(existingUser)

        when:
        userService.updateUser(id, username, password)

        then:
        def exception = thrown(EmptyPasswordException)
        exception.message == "Password cannot be empty"
    }

    def "updateUser should throw UsernameAlreadyTakenException when username is already taken"() {
        given:
        def id = "1"
        def username = "existinguser"
        def password = "newpassword"
        def existingUser = new User(id: id, username: "oldusername", password: "oldpassword")
        userRepository.findById(id) >> Optional.of(existingUser)
        userRepository.existsByUsername(username) >> true

        when:
        userService.updateUser(id, username, password)

        then:
        def exception = thrown(UsernameAlreadyTakenException)
        exception.message == "Username is already taken"
    }

    def "deleteUser should delete an existing user"() {
        given:
        def id = "1"
        def existingUser = new User(id: id, username: "testuser", password: "testpassword")

        when:
        userService.deleteUser(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(existingUser)
        1 * userRepository.deleteById(id)
    }

    def "deleteUser should throw EntityNotFoundException when user with the given id does not exist"() {
        given:
        def id = "1"
        userRepository.findById(id) >> Optional.empty()

        when:
        userService.deleteUser(id)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "User not found"
    }

    def "getUserById should return UserDTO for the given id"() {
        given:
        def id = "1"
        def existingUser = new User(id: id, username: "testuser", password: "testpassword")
        def expectedUserDTO = new UserDTO(id: id, username: "testuser")

        when:
        UserDTO result = userService.getUserById(id)

        then:
        1 * userRepository.findById(id) >> Optional.of(existingUser)
        1 * userMapper.toDto(existingUser) >> expectedUserDTO
        result == expectedUserDTO
    }

    def "getUserById should throw EntityNotFoundException when user with the given id does not exist"() {
        given:
        def id = "1"
        userRepository.findById(id) >> Optional.empty()

        when:
        userService.getUserById(id)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "User not found"
    }

    def "getUserByUsername should return UserDTO for the given username"() {
        given:
        def username = "testuser"
        def existingUser = new User(id: "1", username: username, password: "testpassword")
        def expectedUserDTO = new UserDTO(id: "1", username: username)

        when:
        UserDTO result = userService.getUserByUsername(username)

        then:
        1 * userRepository.findByUsername(username) >> Optional.of(existingUser)
        1 * userMapper.toDto(existingUser) >> expectedUserDTO
        result == expectedUserDTO
    }

    def "getUserByUsername should throw EntityNotFoundException when user with the given username does not exist"() {
        given:
        def username = "testuser"
        userRepository.findByUsername(username) >> Optional.empty()

        when:
        userService.getUserByUsername(username)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "User not found"
    }
}
