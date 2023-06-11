package com.github.vitaliibaranetskyi.socialbird.service

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO

interface CommentService {
    CommentDTO createComment(String content, String authorId, String postId)
    void deleteComment(String id)
    List<CommentDTO> getCommentsByPost(String postId)
    void deleteCommentsByPost(String postId)
}
