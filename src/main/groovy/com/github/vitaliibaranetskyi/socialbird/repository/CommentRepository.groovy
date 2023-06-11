package com.github.vitaliibaranetskyi.socialbird.repository

import com.github.vitaliibaranetskyi.socialbird.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(String postId)
}