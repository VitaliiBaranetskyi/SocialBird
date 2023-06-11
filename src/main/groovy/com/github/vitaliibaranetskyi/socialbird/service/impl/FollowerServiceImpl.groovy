package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.FollowerDTO
import com.github.vitaliibaranetskyi.socialbird.exception.DuplicateFollowException
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.mapper.FollowerMapper
import com.github.vitaliibaranetskyi.socialbird.model.Follower
import com.github.vitaliibaranetskyi.socialbird.repository.FollowerRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.FollowerService
import org.springframework.stereotype.Service

@Service
class FollowerServiceImpl implements FollowerService {
    private final FollowerRepository followerRepository
    private final FollowerMapper followerMapper
    private final UserRepository userRepository

    FollowerServiceImpl(FollowerRepository followerRepository, FollowerMapper followerMapper, UserRepository userRepository) {
        this.followerRepository = followerRepository
        this.followerMapper = followerMapper
        this.userRepository = userRepository
    }

    void followUser(String followerId, String followeeId) {
        boolean isAlreadyFollowing = followerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)
        if (isAlreadyFollowing) {
            throw new DuplicateFollowException("User is already following the specified user")
        }

        userRepository.findById(followerId)
                .orElseThrow({ new EntityNotFoundException("Follower not found") })

        userRepository.findById(followeeId)
                .orElseThrow({ new EntityNotFoundException("Followee not found") })

        Follower follower = new Follower()
        follower.followerId = followerId
        follower.followeeId = followeeId
        followerRepository.save(follower)
    }

    void unfollowUser(String followerId, String followeeId) {
        Follower follower = followerRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow({ new EntityNotFoundException("Follower or Followee not found") })

        followerRepository.delete(follower)
    }

    List<FollowerDTO> getFollowersByUser(String userId) {
        List<Follower> followers = followerRepository.findByFolloweeId(userId)
        if (followers.isEmpty()) {
            throw new EntityNotFoundException("Followers not found")
        }
        followerMapper.toDtoList(followers)
    }

    List<FollowerDTO> getFolloweesByUser(String userId) {
        List<Follower> followees = followerRepository.findByFollowerId(userId)
        if (followees.isEmpty()) {
            throw new EntityNotFoundException("Followees not found")
        }
        followerMapper.toDtoList(followees)
    }
}
