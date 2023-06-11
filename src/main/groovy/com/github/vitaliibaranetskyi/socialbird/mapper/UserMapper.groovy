package com.github.vitaliibaranetskyi.socialbird.mapper

import com.github.vitaliibaranetskyi.socialbird.dto.UserDTO
import com.github.vitaliibaranetskyi.socialbird.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {

    UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO()
        userDTO.id = user.id
        userDTO.username = user.username
        userDTO
    }

    User toEntity(UserDTO userDTO) {
        User user = new User()
        user.id = userDTO.id
        user.username = userDTO.username
        user
    }

    List<UserDTO> toDtoList(List<User> users) {
        users.collect(this.&toDto) as List<UserDTO>
    }

    List<User> toEntityList(List<UserDTO> userDTOs) {
        userDTOs.collect(this.&toEntity) as List<User>
    }
}
