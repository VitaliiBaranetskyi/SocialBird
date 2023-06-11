package com.github.vitaliibaranetskyi.socialbird.controller

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO
import com.github.vitaliibaranetskyi.socialbird.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/comments")
class CommentController {
    private final CommentService commentService

    CommentController(CommentService commentService) {
        this.commentService = commentService
    }

    @PostMapping
    ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO.content, commentDTO.authorId, commentDTO.postId)
        ResponseEntity.status(HttpStatus.CREATED).body(createdComment)
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        commentService.deleteComment(commentId)
        ResponseEntity.noContent().build()
    }

    @GetMapping("/post/{postId}")
    ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable String postId) {
        List<CommentDTO> comments = commentService.getCommentsByPost(postId)
        ResponseEntity.ok(comments)
    }
}

