package com.github.vitaliibaranetskyi.socialbird.repository

import com.github.vitaliibaranetskyi.socialbird.model.Post
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByAuthorId(String authorId)
    List<Post> findByAuthorIdIn(List<String> authorIds)
}