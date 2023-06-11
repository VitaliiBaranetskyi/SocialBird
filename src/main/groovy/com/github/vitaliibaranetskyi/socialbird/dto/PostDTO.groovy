package com.github.vitaliibaranetskyi.socialbird.dto

class PostDTO {
    String id
    String content
    String authorId
    List<LikeDTO> likes = new ArrayList<>()
    List<CommentDTO> comments = new ArrayList<>()
}