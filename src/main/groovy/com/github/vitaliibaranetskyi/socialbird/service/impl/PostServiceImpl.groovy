package com.github.vitaliibaranetskyi.socialbird.service.impl

import com.github.vitaliibaranetskyi.socialbird.dto.CommentDTO
import com.github.vitaliibaranetskyi.socialbird.dto.LikeDTO
import com.github.vitaliibaranetskyi.socialbird.dto.PostDTO
import com.github.vitaliibaranetskyi.socialbird.exception.EntityNotFoundException
import com.github.vitaliibaranetskyi.socialbird.exception.IllegalPostContent
import com.github.vitaliibaranetskyi.socialbird.mapper.PostMapper
import com.github.vitaliibaranetskyi.socialbird.model.Follower
import com.github.vitaliibaranetskyi.socialbird.model.Post
import com.github.vitaliibaranetskyi.socialbird.repository.FollowerRepository
import com.github.vitaliibaranetskyi.socialbird.repository.PostRepository
import com.github.vitaliibaranetskyi.socialbird.repository.UserRepository
import com.github.vitaliibaranetskyi.socialbird.service.CommentService
import com.github.vitaliibaranetskyi.socialbird.service.LikeService
import com.github.vitaliibaranetskyi.socialbird.service.PostService
import org.springframework.stereotype.Service

@Service
class PostServiceImpl implements PostService {
    private final PostRepository postRepository
    private final FollowerRepository followerRepository
    private final UserRepository userRepository
    private final CommentService commentService
    private final LikeService likeService
    private final PostMapper postMapper

    PostServiceImpl(PostRepository postRepository, PostMapper postMapper,
                    FollowerRepository followerRepository, CommentService commentService,
                    UserRepository userRepository, LikeService likeService) {
        this.postRepository = postRepository
        this.postMapper = postMapper
        this.followerRepository = followerRepository
        this.userRepository = userRepository
        this.commentService = commentService
        this.likeService = likeService
    }

    PostDTO createPost(String content, String authorId) {
        if (content.length() < 10) {
            throw new IllegalPostContent("Post content should be at least 10 characters long")
        }

        if (!userRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found")
        }

        Post post = new Post()
        post.content = content
        post.authorId = authorId
        Post createdPost = postRepository.save(post)
        postMapper.toDto(createdPost)
    }

    PostDTO updatePost(String id, String content) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow({ new EntityNotFoundException("Post not found") })

        if (content.length() < 10) {
            throw new IllegalPostContent("Post content should be at least 10 characters long")
        }

        existingPost.content = content
        Post updatedPost = postRepository.save(existingPost)
        postMapper.toDto(updatedPost)
    }

    void deletePost(String id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow({ new EntityNotFoundException("Post not found") })

        // Deleting comments
        commentService.deleteCommentsByPost(id)

        // Deleting likes
        likeService.deleteLikesByPost(id)

        postRepository.delete(existingPost)
    }

    List<PostDTO> getPostsByAuthor(String authorId) {
        if (!userRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author not found")
        }
        List<Post> posts = postRepository.findByAuthorId(authorId)
        List<PostDTO> feed = postMapper.toDtoList(posts)

        enrichPostDTOsWithLikesAndComments(feed)

        feed
    }

    List<PostDTO> getFeedByUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found")
        }

        List<Follower> followers = followerRepository.findByFollowerId(userId)

        List<String> followeeIds = followers.collect({ it.followeeId }) as List<String>

        List<Post> posts = postRepository.findByAuthorIdIn(followeeIds)
        List<PostDTO> feed = postMapper.toDtoList(posts)

        enrichPostDTOsWithLikesAndComments(feed)

        feed
    }

    private void enrichPostDTOsWithLikesAndComments(List<PostDTO> postDTOs) {
        postDTOs.each { postDTO ->
            List<LikeDTO> postLikes = likeService.getLikesByPost(postDTO.id)
            List<CommentDTO> postComments = commentService.getCommentsByPost(postDTO.id)
            postDTO.likes = postLikes
            postDTO.comments = postComments
        }
    }
}
