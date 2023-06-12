package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO
import com.github.vitaliibaranetskyi.socialbird.dto.LikeDTO
import com.github.vitaliibaranetskyi.socialbird.dto.PostDTO
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.exception.IllegalPostContent
import com.github.vitaliibaranetskyi.socialbird.mapper.LikeMapper
import com.github.vitaliibaranetskyi.socialbird.mapper.PostMapper
import com.github.vitaliibaranetskyi.socialbird.model.Follower
import com.github.vitaliibaranetskyi.socialbird.model.Post
import com.github.vitaliibaranetskyi.socialbird.repository.FollowerRepository
import com.github.vitaliibaranetskyi.socialbird.repository.LikeRepository
import com.github.vitaliibaranetskyi.socialbird.repository.PostRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.CommentService
import com.github.vitaliibaranetskyi.socialbird.service.LikeService
import com.github.vitaliibaranetskyi.socialbird.service.PostService
import spock.lang.Specification

class PostServiceImplSpec extends Specification{

    PostRepository postRepository
    FollowerRepository followerRepository
    UserRepository userRepository
    CommentService commentService
    LikeService likeService
    PostMapper postMapper

    PostService postService

    def setup() {
        postRepository = Mock()
        followerRepository = Mock()
        userRepository = Mock()
        commentService = Mock()
        likeService = Mock()
        postMapper = Mock()
        postService = new PostServiceImpl(postRepository, postMapper, followerRepository, commentService, userRepository, likeService)
    }

    def "createPost should create and return a new post"() {
        given:
        def content = "This is a new post."
        def authorId = "1"
        def authorExists = true

        def savedPost = new Post(id: "1", content: content, authorId: authorId)
        def postDTO = new PostDTO(id: "1", content: content, authorId: authorId)

        when:
        PostDTO result = postService.createPost(content, authorId)

        then:
        1 * userRepository.existsById(authorId) >> authorExists
        1 * postRepository.save(_) >> savedPost
        1 * postMapper.toDto(savedPost) >> postDTO

        result == postDTO
    }


    def "createPost should throw IllegalPostContent exception when post content is too short"() {
        given:
        def content = "Short"
        def authorId = "1"
        def authorExists = true

        userRepository.existsById(authorId) >> authorExists

        when:
        postService.createPost(content, authorId)

        then:
        0 * postRepository.save(_)
        0 * postMapper.toDto(_)
        def exception = thrown(IllegalPostContent)
        exception.message == "Post content should be at least 10 characters long"
    }

    def "createPost should throw EntityNotFoundException when author does not exist"() {
        given:
        def content = "This is a new post."
        def authorId = "1"
        def authorExists = false

        userRepository.existsById(authorId) >> authorExists

        when:
        postService.createPost(content, authorId)

        then:
        0 * postRepository.save(_)
        0 * postMapper.toDto(_)
        def exception = thrown(EntityNotFoundException)
        exception.message == "Author not found"
    }

    def "updatePost should update and return the modified post"() {
        given:
        def postId = "1"
        def content = "Updated post content."
        def existingPost = new Post(id: postId, content: "Old content", authorId: "1")
        def updatedPost = new Post(id: postId, content: content, authorId: "1")
        def postDTO = new PostDTO(id: postId, content: content, authorId: "1")

        when:
        PostDTO result = postService.updatePost(postId, content)

        then:
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * postRepository.save(_) >> updatedPost
        1 * postMapper.toDto(updatedPost) >> postDTO

        result == postDTO
    }

    def "updatePost should throw IllegalPostContent exception when post content is too short"() {
        given:
        def postId = "1"
        def content = "Short"
        def existingPost = new Post(id: postId, content: "Old content", authorId: "1")

        postRepository.findById(postId) >> Optional.of(existingPost)

        when:
        postService.updatePost(postId, content)

        then:
        0 * postRepository.save(_)
        0 * postMapper.toDto(_)
        def exception = thrown(IllegalPostContent)
        exception.message == "Post content should be at least 10 characters long"
    }

    def "updatePost should throw EntityNotFoundException when post does not exist"() {
        given:
        def postId = "1"
        def content = "Updated post content."

        postRepository.findById(postId) >> Optional.empty()

        when:
        postService.updatePost(postId, content)

        then:
        0 * postRepository.save(_)
        0 * postMapper.toDto(_)
        def exception = thrown(EntityNotFoundException)
        exception.message == "Post not found"
    }

    def "deletePost should delete the post and its associated comments and likes"() {
        given:
        def postId = "1"
        def existingPost = new Post(id: postId, content: "Post content", authorId: "1")

        commentService.deleteCommentsByPost(postId) >> {}

        likeService.deleteLikesByPost(postId) >> {}

        when:
        postService.deletePost(postId)

        then:
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * commentService.deleteCommentsByPost(postId)
        1 * likeService.deleteLikesByPost(postId)
        1 * postRepository.delete(existingPost)
    }

    def "deletePost should throw EntityNotFoundException when post does not exist"() {
        given:
        def postId = "1"

        postRepository.findById(postId) >> Optional.empty()

        when:
        postService.deletePost(postId)

        then:
        0 * commentService.deleteCommentsByPost(_)
        0 * likeService.deleteLikesByPost(_)
        0 * postRepository.delete(_)
        def exception = thrown(EntityNotFoundException)
        exception.message == "Post not found"
    }

    def "getPostsByAuthor should return posts by author with likes and comments"() {
        given:
        def authorId = "1"
        def authorExists = true

        userRepository.existsById(authorId) >> authorExists

        def posts = [new Post(id: "1", content: "Post 1", authorId: authorId),
                     new Post(id: "2", content: "Post 2", authorId: authorId)]

        def postDTOs = [new PostDTO(id: "1", content: "Post 1", authorId: authorId),
                        new PostDTO(id: "2", content: "Post 2", authorId: authorId)]

        def postLikes = [[new LikeDTO(id: "1", userId: "1", postId: "1")],
                         [new LikeDTO(id: "2", userId: "2", postId: "2")]]

        def postComments = [[new CommentDTO(id: "1", content: "Comment 1", authorId: "1", postId: "1")],
                            [new CommentDTO(id: "2", content: "Comment 2", authorId: "2", postId: "2")]]

        when:
        List<PostDTO> result = postService.getPostsByAuthor(authorId)

        then:
        1 * userRepository.existsById(authorId) >> authorExists
        1 * postRepository.findByAuthorId(authorId) >> posts
        1 * postMapper.toDtoList(posts) >> postDTOs
        1 * likeService.getLikesByPost("1") >> postLikes[0]
        1 * likeService.getLikesByPost("2") >> postLikes[1]
        1 * commentService.getCommentsByPost("1") >> postComments[0]
        1 * commentService.getCommentsByPost("2") >> postComments[1]

        result.size() == 2
        result[0] == postDTOs[0]
        result[1] == postDTOs[1]
        result[0].likes == postLikes[0]
        result[1].likes == postLikes[1]
        result[0].comments == postComments[0]
        result[1].comments == postComments[1]
    }

    def "getPostsByAuthor should throw EntityNotFoundException when author does not exist"() {
        given:
        def authorId = "1"
        def authorExists = false

        userRepository.existsById(authorId) >> authorExists

        when:
        postService.getPostsByAuthor(authorId)

        then:
        0 * postRepository.findByAuthorId(_)
        0 * postMapper.toDtoList(_)
        0 * likeService.getLikesByPost(_)
        0 * commentService.getCommentsByPost(_)
        def exception = thrown(EntityNotFoundException)
        exception.message == "Author not found"
    }

    def "getFeedByUser should return feed for the user with likes and comments"() {
        given:
        def userId = "1"
        def userExists = true

        userRepository.existsById(userId) >> userExists

        def followers = [new Follower(id: "1", followerId: userId, followeeId: "2"),
                         new Follower(id: "2", followerId: userId, followeeId: "3")]

        def followeeIds = ["2", "3"]

        def posts = [new Post(id: "1", content: "Post 1", authorId: "2"),
                     new Post(id: "2", content: "Post 2", authorId: "3")]

        def postDTOs = [new PostDTO(id: "1", content: "Post 1", authorId: "2"),
                        new PostDTO(id: "2", content: "Post 2", authorId: "3")]

        def postLikes = [[new LikeDTO(id: "1", userId: "1", postId: "1")],
                         [new LikeDTO(id: "2", userId: "1", postId: "2")]]

        def postComments = [[new CommentDTO(id: "1", content: "Comment 1", authorId: "1", postId: "1")],
                            [new CommentDTO(id: "2", content: "Comment 2", authorId: "1", postId: "2")]]
        followers*.followeeId >> followeeIds

        when:
        List<PostDTO> result = postService.getFeedByUser(userId)

        then:
        1 * userRepository.existsById(userId) >> userExists
        1 * followerRepository.findByFollowerId(userId) >> followers
        1 * postRepository.findByAuthorIdIn(followeeIds) >> posts
        1 * postMapper.toDtoList(posts) >> postDTOs
        1 * likeService.getLikesByPost("1") >> postLikes[0]
        1 * likeService.getLikesByPost("2") >> postLikes[1]
        1 * commentService.getCommentsByPost("1") >> postComments[0]
        1 * commentService.getCommentsByPost("2") >> postComments[1]

        result.size() == 2
        result[0] == postDTOs[0]
        result[1] == postDTOs[1]
        result[0].likes == postLikes[0]
        result[1].likes == postLikes[1]
        result[0].comments == postComments[0]
        result[1].comments == postComments[1]
    }

    def "getFeedByUser should throw EntityNotFoundException when user does not exist"() {
        given:
        def userId = "1"
        def userExists = false

        userRepository.existsById(userId) >> userExists

        when:
        postService.getFeedByUser(userId)

        then:
        0 * followerRepository.findByFollowerId(_)
        0 * postRepository.findByAuthorIdIn(_)
        0 * postMapper.toDtoList(_)
        0 * likeService.getLikesByPost(_)
        0 * commentService.getCommentsByPost(_)
        def exception = thrown(EntityNotFoundException)
        exception.message == "User not found"
    }
}
