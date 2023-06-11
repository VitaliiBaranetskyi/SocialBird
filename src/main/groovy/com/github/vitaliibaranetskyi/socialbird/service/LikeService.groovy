package com.github.vitaliibaranetskyi.socialbird.service

import com.github.vitaliibaranetskyi.socialbird.dto.LikeDTO

interface LikeService {
    LikeDTO likePost(String userId, String postId)
    void unlikePost(String userId, String postId)
    List<LikeDTO> getLikesByPost(String postId)
    void deleteLikesByPost(String postId)
}
