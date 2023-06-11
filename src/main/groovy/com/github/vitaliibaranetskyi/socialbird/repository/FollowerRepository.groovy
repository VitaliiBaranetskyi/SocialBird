package com.github.vitaliibaranetskyi.socialbird.repository

import com.github.vitaliibaranetskyi.socialbird.model.Follower
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowerRepository extends MongoRepository<Follower, String> {
    List<Follower> findByFolloweeId(String followeeId)

    List<Follower> findByFollowerId(String followerId)

    boolean existsByFollowerIdAndFolloweeId (String followerId, String followeeId)

    Optional<Follower> findByFollowerIdAndFolloweeId(String followerId, String followeeId)
}