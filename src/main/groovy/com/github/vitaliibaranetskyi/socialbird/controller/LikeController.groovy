package com.github.vitaliibaranetskyi.socialbird.controller

import com.github.vitaliibaranetskyi.socialbird.dto.LikeDTO
import com.github.vitaliibaranetskyi.socialbird.mapper.LikeMapper
import com.github.vitaliibaranetskyi.socialbird.service.LikeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/likes")
class LikeController {

    private final LikeService likeService
    private final LikeMapper likeMapper

    LikeController(LikeService likeService, LikeMapper likeMapper) {
        this.likeService = likeService
        this.likeMapper = likeMapper
    }

    @PostMapping
    ResponseEntity<LikeDTO> likePost(@RequestParam String userId, @RequestParam String postId) {
        LikeDTO likeDTO = likeService.likePost(userId, postId)
        ResponseEntity.ok(likeDTO)
    }

    @DeleteMapping
    ResponseEntity<Void> unlikePost(@RequestParam String userId, @RequestParam String postId) {
        likeService.unlikePost(userId, postId)
        ResponseEntity.noContent().build()
    }

    @GetMapping("/post/{postId}")
    ResponseEntity<List<LikeDTO>> getLikesByPost(@PathVariable String postId) {
        List<LikeDTO> likeDTOs = likeService.getLikesByPost(postId)
        ResponseEntity.ok(likeDTOs)
    }
}


