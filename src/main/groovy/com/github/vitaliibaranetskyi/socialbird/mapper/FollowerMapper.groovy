package com.github.vitaliibaranetskyi.socialbird.mapper

import com.github.vitaliibaranetskyi.socialbird.dto.FollowerDTO
import com.github.vitaliibaranetskyi.socialbird.model.Follower
import org.springframework.stereotype.Component

@Component
class FollowerMapper {


    FollowerDTO toDto(Follower follower) {
        FollowerDTO followerDTO = new FollowerDTO()
        followerDTO.id = follower.id
        followerDTO.followerId = follower.followerId
        followerDTO.followeeId = follower.followeeId
        followerDTO
    }

    Follower toEntity(FollowerDTO followerDTO) {
        Follower follower = new Follower()
        follower.id = followerDTO.id
        follower.followerId = followerDTO.followerId
        follower.followeeId = followerDTO.followeeId
        follower
    }

    List<FollowerDTO> toDtoList(List<Follower> followers) {
        followers.collect(this.&toDto) as List<FollowerDTO>
    }

    List<Follower> toEntityList(List<FollowerDTO> followerDTOs) {
        followerDTOs.collect(this.&toEntity) as List<Follower>
    }
}
