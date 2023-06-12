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
import spock.lang.Specification

class LikeServiceImplSpec extends Specification {

    LikeRepository likeRepository
    UserRepository userRepository
    PostRepository postRepository
    LikeMapper likeMapper

    LikeService likeService

    def setup() {
        userRepository = Mock()
        likeRepository = Mock()
        postRepository = Mock()
        likeMapper = Mock()
        likeService = new LikeServiceImpl(likeRepository, postRepository, userRepository, likeMapper)
    }

    def "likePost should create a new like for the post"() {
        given:
        def userId = "1"
        def postId = "2"
        def existingPost = new Post(id: postId, content: "Test post")
        def existingUser = new User(id: userId, username: "user1")
        def existingLike = null
        def savedLike = new Like(id: "3", userId: userId, postId: postId)
        def expectedLikeDTO = new LikeDTO(id: "3", userId: userId, postId: postId)

        when:
        LikeDTO result = likeService.likePost(userId, postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * userRepository.findById(userId) >> Optional.of(existingUser)
        1 * likeRepository.findByPostIdAndUserId(postId, userId) >> existingLike
        1 * likeRepository.save(_ as Like) >> savedLike
        1 * likeMapper.toDto(savedLike) >> expectedLikeDTO
        result == expectedLikeDTO
    }

    def "likePost should throw EntityNotFoundException when post is not found"() {
        given:
        def userId = "1"
        def postId = "2"

        when:
        likeService.likePost(userId, postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Post not found"
        1 * postRepository.findById(postId) >> Optional.empty()
        0 * userRepository.findById(_)
        0 * likeRepository.findByPostIdAndUserId(_, _)
        0 * likeRepository.save(_ as Like)
        0 * likeMapper.toDto(_)
    }

    def "likePost should throw EntityNotFoundException when user is not found"() {
        given:
        def userId = "1"
        def postId = "2"
        def existingPost = new Post(id: postId, content: "Test post")

        when:
        likeService.likePost(userId, postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "User not found"
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * userRepository.findById(userId) >> Optional.empty()
        0 * likeRepository.findByPostIdAndUserId(_, _)
        0 * likeRepository.save(_ as Like)
        0 * likeMapper.toDto(_)
    }

    def "likePost should throw AlreadyLikedException when post is already liked by the user"() {
        given:
        def userId = "1"
        def postId = "2"
        def existingPost = new Post(id: postId, content: "Test post")
        def existingUser = new User(id: userId, username: "user1")
        def existingLike = new Like(id: "3", userId: userId, postId: postId)

        when:
        likeService.likePost(userId, postId)

        then:
        def exception = thrown(AlreadyLikedException)
        exception.message == "Post already liked by the user"
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * userRepository.findById(userId) >> Optional.of(existingUser)
        1 * likeRepository.findByPostIdAndUserId(postId, userId) >> existingLike
        0 * likeRepository.save(_ as Like)
        0 * likeMapper.toDto(_)
    }

    def "unlikePost should delete the like for the post"() {
        given:
        def userId = "1"
        def postId = "2"
        def existingLike = new Like(id: "3", userId: userId, postId: postId)

        when:
        likeService.unlikePost(userId, postId)

        then:
        1 * likeRepository.findByPostIdAndUserId(postId, userId) >> existingLike
        1 * likeRepository.delete(existingLike)
    }

    def "unlikePost should throw EntityNotFoundException when like is not found"() {
        given:
        def userId = "1"
        def postId = "2"

        when:
        likeService.unlikePost(userId, postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "User or Like not found"
        1 * likeRepository.findByPostIdAndUserId(postId, userId) >> null
        0 * likeRepository.delete(_ as Like)
    }

    def "getLikesByPost should return a list of likes for the post"() {
        given:
        def postId = "1"
        def existingPost = new Post(id: postId, content: "Test post")
        def existingLikes = [
                new Like(id: "1", userId: "1", postId: postId),
                new Like(id: "2", userId: "2", postId: postId)
        ]
        def expectedLikeDTOs = [
                new LikeDTO(id: "1", userId: "1", postId: postId),
                new LikeDTO(id: "2", userId: "2", postId: postId)
        ]

        when:
        List<LikeDTO> result = likeService.getLikesByPost(postId)

        then:
        result == expectedLikeDTOs
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * likeRepository.findByPostId(postId) >> existingLikes
        1 * likeMapper.toDtoList(existingLikes) >> expectedLikeDTOs
    }

    def "getLikesByPost should throw EntityNotFoundException when post is not found"() {
        given:
        def postId = "1"

        when:
        likeService.getLikesByPost(postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Post not found"
        1 * postRepository.findById(postId) >> Optional.empty()
        0 * likeRepository.findByPostId(_)
        0 * likeMapper.toDtoList(_)
    }

    def "deleteLikesByPost should delete all the likes for the post"() {
        given:
        def postId = "1"
        def existingLikes = [
                new Like(id: "1", userId: "1", postId: postId),
                new Like(id: "2", userId: "2", postId: postId)
        ]

        when:
        likeService.deleteLikesByPost(postId)

        then:
        1 * likeRepository.findByPostId(postId) >> existingLikes
        1 * likeRepository.deleteAll(existingLikes)
    }
}
