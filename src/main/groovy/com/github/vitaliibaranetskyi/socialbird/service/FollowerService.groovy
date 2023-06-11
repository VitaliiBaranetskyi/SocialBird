package com.github.vitaliibaranetskyi.socialbird.service

import com.github.vitaliibaranetskyi.socialbird.dto.FollowerDTO

interface FollowerService {
    void followUser(String followerId, String followeeId)
    void unfollowUser(String followerId, String followeeId)
    List<FollowerDTO> getFollowersByUser(String userId)
    List<FollowerDTO> getFolloweesByUser(String userId)
}
