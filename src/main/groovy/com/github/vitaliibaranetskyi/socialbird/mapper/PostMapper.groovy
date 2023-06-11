package com.github.vitaliibaranetskyi.socialbird.mapper

import com.github.vitaliibaranetskyi.socialbird.dto.PostDTO
import com.github.vitaliibaranetskyi.socialbird.model.Post
import org.springframework.stereotype.Component

@Component
class PostMapper {

    PostDTO toDto(Post post) {
        PostDTO postDTO = new PostDTO()
        postDTO.id = post.id
        postDTO.content = post.content
        postDTO.authorId = post.authorId
        postDTO
    }

    Post toEntity(PostDTO postDTO) {
        Post post = new Post()
        post.id = postDTO.id
        post.content = postDTO.content
        post.authorId = postDTO.authorId
        post
    }

    List<PostDTO> toDtoList(List<Post> posts) {
        posts.collect(this.&toDto) as List<PostDTO>
    }

    List<Post> toEntityList(List<PostDTO> postDTOs) {
        postDTOs.collect(this.&toEntity) as List<Post>
    }
}
