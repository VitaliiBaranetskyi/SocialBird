package com.github.vitaliibaranetskyi.socialbird.controller

import com.github.vitaliibaranetskyi.socialbird.dto.PostDTO
import com.github.vitaliibaranetskyi.socialbird.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController {
    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @PostMapping
    ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPost = postService.createPost(postDTO.getContent(), postDTO.getAuthorId())
        new ResponseEntity<>(createdPost, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    ResponseEntity<PostDTO> updatePost(@PathVariable String id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(id, postDTO.getContent())
        ResponseEntity.ok(updatedPost)
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id)
        ResponseEntity.noContent().build()
    }

    @GetMapping("/author/{authorId}")
    ResponseEntity<List<PostDTO>> getPostsByAuthor(@PathVariable String authorId) {
        List<PostDTO> posts = postService.getPostsByAuthor(authorId)
        ResponseEntity.ok(posts)
    }

    @GetMapping("/feed/{userId}")
    ResponseEntity<List<PostDTO>> getFeedByUser(@PathVariable String userId) {
        List<PostDTO> feed = postService.getFeedByUser(userId)
        ResponseEntity.ok(feed)
    }
}

