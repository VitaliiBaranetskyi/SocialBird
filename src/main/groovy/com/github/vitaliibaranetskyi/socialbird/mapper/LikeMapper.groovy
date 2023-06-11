package com.github.vitaliibaranetskyi.socialbird.mapper

import com.github.vitaliibaranetskyi.socialbird.dto.LikeDTO
import com.github.vitaliibaranetskyi.socialbird.model.Like
import org.springframework.stereotype.Component

@Component
class LikeMapper {

    LikeDTO toDto(Like like) {
        LikeDTO likeDTO = new LikeDTO()
        likeDTO.id = like.id
        likeDTO.userId = like.userId
        likeDTO.postId = like.postId
        likeDTO
    }

    Like toEntity(LikeDTO likeDTO) {
        Like like = new Like()
        like.id = likeDTO.id
        like.userId = likeDTO.userId
        like.postId = likeDTO.postId
        like
    }

    List<LikeDTO> toDtoList(List<Like> likes) {
        likes.collect(this.&toDto) as List<LikeDTO>
    }

    List<Like> toEntityList(List<LikeDTO> likeDTOs) {
        likeDTOs.collect(this.&toEntity) as List<Like>
    }
}
