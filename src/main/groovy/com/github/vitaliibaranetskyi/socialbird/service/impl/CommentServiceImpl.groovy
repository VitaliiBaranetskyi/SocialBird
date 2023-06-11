package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.exception.IllegalCommentContent
import com.github.vitaliibaranetskyi.socialbird.mapper.CommentMapper
import com.github.vitaliibaranetskyi.socialbird.model.Comment
import com.github.vitaliibaranetskyi.socialbird.repository.CommentRepository
import com.github.vitaliibaranetskyi.socialbird.repository.PostRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.CommentService
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository
    private final PostRepository postRepository
    private final UserRepository userRepository
    private final CommentMapper commentMapper

    CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                       UserRepository userRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository
        this.postRepository = postRepository
        this.userRepository = userRepository
        this.commentMapper = commentMapper
    }

    CommentDTO createComment(String content, String authorId, String postId) {
        if (content == null || content.isBlank()) {
            throw new IllegalCommentContent("Comment content cannot be empty or consist only of spaces")
        }

        if (!userRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found")
        }

        postRepository.findById(postId)
                .orElseThrow({ new EntityNotFoundException("Post not found") })

        Comment comment = new Comment()
        comment.content = content
        comment.authorId = authorId
        comment.postId = postId
        Comment createdComment = commentRepository.save(comment)

        commentMapper.toDto(createdComment)
    }

    void deleteComment(String id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow({ new EntityNotFoundException("Comment not found") })

        commentRepository.delete(existingComment)
    }

    List<CommentDTO> getCommentsByPost(String postId) {
        postRepository.findById(postId)
                .orElseThrow({ new EntityNotFoundException("Post not found") })
        List<Comment> comments = commentRepository.findByPostId(postId)
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("Comments not found")
        }

        commentMapper.toDtoList(comments)
    }

    void deleteCommentsByPost(String postId) {
        List<Comment> comments = commentRepository.findByPostId(postId)
        commentRepository.deleteAll(comments)
    }
}

