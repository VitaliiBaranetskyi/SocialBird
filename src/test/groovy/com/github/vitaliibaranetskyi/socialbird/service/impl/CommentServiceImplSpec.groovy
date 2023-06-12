package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.exception.IllegalCommentContent
import com.github.vitaliibaranetskyi.socialbird.mapper.CommentMapper
import com.github.vitaliibaranetskyi.socialbird.mapper.UserMapper
import com.github.vitaliibaranetskyi.socialbird.model.Comment
import com.github.vitaliibaranetskyi.socialbird.model.Post
import com.github.vitaliibaranetskyi.socialbird.repository.CommentRepository
import com.github.vitaliibaranetskyi.socialbird.repository.PostRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.CommentService
import com.github.vitaliibaranetskyi.socialbird.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class CommentServiceImplSpec extends Specification {
    CommentRepository commentRepository
    PostRepository postRepository
    UserRepository userRepository
    CommentMapper commentMapper

    CommentService commentService

    def setup() {
        commentRepository = Mock()
        postRepository = Mock()
        userRepository = Mock()
        commentMapper = Mock()
        commentService = new CommentServiceImpl(
                commentRepository, postRepository, userRepository, commentMapper)
    }

    def "createComment should create a new comment and return the created CommentDTO"() {
        given:
        def content = "Test comment"
        def authorId = "1"
        def postId = "2"
        def createdComment = new Comment(id: "1", content: content, authorId: authorId, postId: postId)
        def expectedCommentDTO = new CommentDTO(id: "1", content: content, authorId: authorId, postId: postId)

        when:
        CommentDTO result = commentService.createComment(content, authorId, postId)

        then:
        1 * userRepository.existsById(authorId) >> true
        1 * postRepository.findById(postId) >> Optional.of(new Post(id: postId, content: "Test post"))
        1 * commentRepository.save(_ as Comment) >> createdComment
        1 * commentMapper.toDto(createdComment) >> expectedCommentDTO
        result == expectedCommentDTO
    }

    def "createComment should throw IllegalCommentContent when comment content is empty"() {
        given:
        def content = ""
        def authorId = "1"
        def postId = "2"
        userRepository.existsById(authorId) >> true

        when:
        commentService.createComment(content, authorId, postId)

        then:
        def exception = thrown(IllegalCommentContent)
        exception.message == "Comment content cannot be empty or consist only of spaces"
        0 * postRepository.findById(_)
        0 * commentRepository.save(_ as Comment)
        0 * commentMapper.toDto(_)
    }

    def "createComment should throw EntityNotFoundException when author is not found"() {
        given:
        def content = "Test comment"
        def authorId = "1"
        def postId = "2"
        userRepository.existsById(authorId) >> false

        when:
        commentService.createComment(content, authorId, postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Author not found"
        0 * postRepository.findById(_)
        0 * commentRepository.save(_ as Comment)
        0 * commentMapper.toDto(_)
    }

    def "createComment should throw EntityNotFoundException when post is not found"() {
        given:
        def content = "Test comment"
        def authorId = "1"
        def postId = "2"

        when:
        commentService.createComment(content, authorId, postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Post not found"
        1 * userRepository.existsById(authorId) >> true
        1 * postRepository.findById(postId) >> Optional.empty()
        0 * commentRepository.save(_ as Comment)
        0 * commentMapper.toDto(_)
    }

    def "deleteComment should delete an existing comment"() {
        given:
        def id = "1"
        def existingComment = new Comment(id: id, content: "Test comment", authorId: "1", postId: "2")

        when:
        commentService.deleteComment(id)

        then:
        1 * commentRepository.findById(id) >> Optional.of(existingComment)
        1 * commentRepository.delete(existingComment)
    }

    def "deleteComment should throw EntityNotFoundException when comment with the given id does not exist"() {
        given:
        def id = "1"
        commentRepository.findById(id) >> Optional.empty()

        when:
        commentService.deleteComment(id)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Comment not found"
    }

    def "getCommentsByPost should return a list of CommentDTO for the given post"() {
        given:
        def postId = "1"
        def existingPost = new Post(id: postId, content: "Test post")
        def existingComments = [
                new Comment(id: "1", content: "Comment 1", authorId: "1", postId: postId),
                new Comment(id: "2", content: "Comment 2", authorId: "2", postId: postId)
        ]
        def expectedCommentDTOs = [
                new CommentDTO(id: "1", content: "Comment 1", authorId: "1", postId: postId),
                new CommentDTO(id: "2", content: "Comment 2", authorId: "2", postId: postId)
        ]


        when:
        List<CommentDTO> result = commentService.getCommentsByPost(postId)

        then:
        result == expectedCommentDTOs
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * commentRepository.findByPostId(postId) >> existingComments
        1 * commentMapper.toDtoList(existingComments) >> expectedCommentDTOs
    }

    def "getCommentsByPost should throw EntityNotFoundException when post with the given id does not exist"() {
        given:
        def postId = "1"

        when:
        commentService.getCommentsByPost(postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Post not found"
        1 * postRepository.findById(postId) >> Optional.empty()
        0 * commentRepository.findByPostId(_)
        0 * commentMapper.toDtoList(_)
    }

    def "getCommentsByPost should throw EntityNotFoundException when no comments are found for the given post"() {
        given:
        def postId = "1"
        def existingPost = new Post(id: postId, content: "Test post")

        when:
        commentService.getCommentsByPost(postId)

        then:
        def exception = thrown(EntityNotFoundException)
        exception.message == "Comments not found"
        1 * postRepository.findById(postId) >> Optional.of(existingPost)
        1 * commentRepository.findByPostId(postId) >> []
        0 * commentMapper.toDtoList(_)
    }

    def "deleteCommentsByPost should delete all comments for the given post"() {
        given:
        def postId = "1"
        def existingComments = [
                new Comment(id: "1", content: "Comment 1", authorId: "1", postId: postId),
                new Comment(id: "2", content: "Comment 2", authorId: "2", postId: postId)
        ]

        when:
        commentService.deleteCommentsByPost(postId)

        then:
        1 * commentRepository.findByPostId(postId) >> existingComments
        1 * commentRepository.deleteAll(existingComments)
    }
}
