package com.github.vitaliibaranetskyi.socialbird.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "followers")
class Follower {
    @Id
    String id
    String followerId
    String followeeId
}
