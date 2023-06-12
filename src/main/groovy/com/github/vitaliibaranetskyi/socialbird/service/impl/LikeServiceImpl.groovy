package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.LikeDTO
import com.github.vitaliibaranetskyi.socialbird.exception.AlreadyLikedException
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.mapper.LikeMapper
import com.github.vitaliibaranetskyi.socialbird.model.Like
import com.github.vitaliibaranetskyi.socialbird.model.Post
import com.github.vitaliibaranetskyi.socialbird.model.User
import com.github.vitaliibaranetskyi.socialbird.repository.LikeRepository
import com.github.vitaliibaranetskyi.socialbird.repository.PostRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.LikeService
import org.springframework.stereotype.Service

@Service
class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository
    protected final PostRepository postRepository
    private final UserRepository userRepository
    private final LikeMapper likeMapper

    LikeServiceImpl(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository, LikeMapper likeMapper) {
        this.likeRepository = likeRepository
        this.postRepository = postRepository
        this.userRepository = userRepository
        this.likeMapper = likeMapper
    }

    LikeDTO likePost(String userId, String postId) {
        postRepository.findById(postId)
                .orElseThrow({ new EntityNotFoundException("Post not found") })

        userRepository.findById(userId)
                .orElseThrow({ new EntityNotFoundException("User not found") })

        Like existingLike = likeRepository.findByPostIdAndUserId(postId, userId)

        if (existingLike != null) {
            throw new AlreadyLikedException("Post already liked by the user")
        }

        Like newLike = new Like()
        newLike.postId = postId
        newLike.userId = userId

        Like savedLike = likeRepository.save(newLike)

        likeMapper.toDto(savedLike)
    }

    void unlikePost(String userId, String postId) {
        Like existingLike = likeRepository.findByPostIdAndUserId(postId, userId)

        if (existingLike == null) {
            throw new EntityNotFoundException("User or Like not found")
        }

        likeRepository.delete(existingLike)
    }

    List<LikeDTO> getLikesByPost(String postId) {
        postRepository.findById(postId)
                .orElseThrow({ new EntityNotFoundException("Post not found") })

        List<Like> likes = likeRepository.findByPostId(postId)

        likeMapper.toDtoList(likes)
    }

    void deleteLikesByPost(String postId) {
        List<Like> likes = likeRepository.findByPostId(postId)
        likeRepository.deleteAll(likes)
    }
}

