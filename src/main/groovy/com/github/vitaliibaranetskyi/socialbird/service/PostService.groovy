package com.github.vitaliibaranetskyi.socialbird.service

import com.github.vitaliibaranetskyi.socialbird.dto.PostDTO

interface PostService {
    PostDTO createPost(String content, String authorId)
    PostDTO updatePost(String id, String content)
    void deletePost(String id)
    List<PostDTO> getPostsByAuthor(String authorId)
    List<PostDTO> getFeedByUser(String userId)
}
