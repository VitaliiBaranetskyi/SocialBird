package com.github.vitaliibaranetskyi.socialbird.repository

import com.github.vitaliibaranetskyi.socialbird.model.Like
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository extends MongoRepository<Like, String> {
    Like findByPostIdAndUserId(String postId, String userId)
    List<Like> findByPostId(String postId)
}