package com.github.vitaliibaranetskyi.socialbird.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "posts")
class Post {
    @Id
    String id
    String content
    String authorId
}
