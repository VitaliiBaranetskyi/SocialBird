package com.github.vitaliibaranetskyi.socialbird.mapper

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO
import com.github.vitaliibaranetskyi.socialbird.model.Comment
import org.springframework.stereotype.Component

@Component
class CommentMapper {

    CommentDTO toDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO()
        commentDTO.id = comment.id
        commentDTO.content = comment.content
        commentDTO.authorId = comment.authorId
        commentDTO.postId = comment.postId
        commentDTO
    }

    Comment toEntity(CommentDTO commentDTO) {
        Comment comment = new Comment()
        comment.id = commentDTO.id
        comment.content = commentDTO.content
        comment.authorId = commentDTO.authorId
        comment.postId = commentDTO.postId
        comment
    }

    List<CommentDTO> toDtoList(List<Comment> comments) {
        comments.collect(this.&toDto) as List<CommentDTO>
    }

    List<Comment> toEntityList(List<CommentDTO> commentDTOs) {
        commentDTOs.collect(this.&toEntity) as List<Comment>
    }
}
