package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.FollowerDTO
import com.github.vitaliibaranetskyi.socialbird.exception.DuplicateFollowException
import com.github.vitaliibaranetskyi.socialbird.exception.EmptyUsernameException
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.mapper.FollowerMapper
import com.github.vitaliibaranetskyi.socialbird.mapper.LikeMapper
import com.github.vitaliibaranetskyi.socialbird.model.Follower
import com.github.vitaliibaranetskyi.socialbird.model.User
import com.github.vitaliibaranetskyi.socialbird.repository.FollowerRepository
import com.github.vitaliibaranetskyi.socialbird.repository.LikeRepository
import com.github.vitaliibaranetskyi.socialbird.repository.PostRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.FollowerService
import com.github.vitaliibaranetskyi.socialbird.service.LikeService
import spock.lang.Specification

class FollowerServiceImplSpec extends Specification{

    FollowerRepository followerRepository
    UserRepository userRepository
    FollowerMapper followerMapper

    FollowerService followerService

    def setup() {
        followerRepository = Mock()
        userRepository = Mock()
        followerMapper = Mock()
        followerService = new FollowerServiceImpl(followerRepository, followerMapper, userRepository)
    }

    def "followUser should create a new follower relationship"() {
        given:
        def followerId = "1"
        def followeeId = "2"
        def follower = new Follower(id: "3", followerId: followerId, followeeId: followeeId)

        followerMapper.toDtoList([follower]) >> [new FollowerDTO(id: "3", followerId: followerId, followeeId: followeeId)]

        when:
        followerService.followUser(followerId, followeeId)

        then:
        1 * followerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId) >> false
        1 * userRepository.findById(followerId) >> Optional.of(new User(id: followerId))
        1 * userRepository.findById(followeeId) >> Optional.of(new User(id: followeeId))
        1 * followerRepository.save(_ as Follower)
    }

    def "followUser should throw DuplicateFollowException when the follower relationship already exists"() {
        given:
        def followerId = "1"
        def followeeId = "2"

        userRepository.findById(followerId) >> Optional.of(new User(id: followerId))
        userRepository.findById(followeeId) >> Optional.of(new User(id: followeeId))
        followerRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId) >> true

        when:
        followerService.followUser(followerId, followeeId)

        then:
        def exception = thrown(DuplicateFollowException)
        exception.message == "User is already following the specified user"
    }

    def "followUser should throw EntityNotFoundException when follower is not found"() {
        given:
        def followerId = "1"
        def followeeId = "2"

        userRepository.findById(followerId) >> Optional.empty()
        userRepository.findById(followeeId) >> Optional.of(new User(id: followeeId))

        when:
        followerService.followUser(followerId, followeeId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Follower not found"
    }

    def "followUser should throw EntityNotFoundException when followee is not found"() {
        given:
        def followerId = "1"
        def followeeId = "2"

        userRepository.findById(followerId) >> Optional.of(new User(id: followerId))
        userRepository.findById(followeeId) >> Optional.empty()

        when:
        followerService.followUser(followerId, followeeId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Followee not found"
    }

    def "unfollowUser should delete the follower relationship"() {
        given:
        def followerId = "1"
        def followeeId = "2"
        def follower = new Follower(id: "3", followerId: followerId, followeeId: followeeId)

        when:
        followerService.unfollowUser(followerId, followeeId)

        then:
        1 * followerRepository.findByFollowerIdAndFolloweeId(followerId, followeeId) >> Optional.of(follower)
        1 * followerRepository.delete(follower)
    }

    def "unfollowUser should throw EntityNotFoundException when the follower relationship does not exist"() {
        given:
        def followerId = "1"
        def followeeId = "2"

        followerRepository.findByFollowerIdAndFolloweeId(followerId, followeeId) >> Optional.empty()

        when:
        followerService.unfollowUser(followerId, followeeId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Follower or Followee not found"
    }

    def "getFollowersByUser should return the list of follower DTOs"() {
        given:
        def userId = "1"
        def follower1 = new Follower(id: "2", followerId: "3", followeeId: userId)
        def follower2 = new Follower(id: "4", followerId: "5", followeeId: userId)

        when:
        List<FollowerDTO> result = followerService.getFollowersByUser(userId)

        then:
        1 * followerRepository.findByFolloweeId(userId) >> [follower1, follower2]
        1 * followerMapper.toDtoList([follower1, follower2]) >> [new FollowerDTO(id: "2", followerId: "3", followeeId: userId), new FollowerDTO(id: "4", followerId: "5", followeeId: userId)]
        result.size() == 2
    }

    def "getFollowersByUser should throw EntityNotFoundException when no followers are found"() {
        given:
        def userId = "1"

        followerRepository.findByFolloweeId(userId) >> []

        when:
        followerService.getFollowersByUser(userId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Followers not found"
    }

    def "getFolloweesByUser should return the list of followee DTOs"() {
        given:
        def userId = "1"
        def followee1 = new Follower(id: "2", followerId: userId, followeeId: "3")
        def followee2 = new Follower(id: "4", followerId: userId, followeeId: "5")

        when:
        List<FollowerDTO> result = followerService.getFolloweesByUser(userId)

        then:
        1 * followerRepository.findByFollowerId(userId) >> [followee1, followee2]
        1 * followerMapper.toDtoList([followee1, followee2]) >> [new FollowerDTO(id: "2", followerId: userId, followeeId: "3"), new FollowerDTO(id: "4", followerId: userId, followeeId: "5")]
        result.size() == 2
    }

    def "getFolloweesByUser should throw EntityNotFoundException when no followees are found"() {
        given:
        def userId = "1"

        followerRepository.findByFollowerId(userId) >> []

        when:
        followerService.getFolloweesByUser(userId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Followees not found"
    }
}
