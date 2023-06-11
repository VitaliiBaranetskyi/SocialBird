package com.github.vitaliibaranetskyi.socialbird.controller

import com.github.vitaliibaranetskyi.socialbird.dto.FollowerDTO
import com.github.vitaliibaranetskyi.socialbird.service.FollowerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/followers")
class FollowerController {
    private final FollowerService followerService

    FollowerController(FollowerService followerService) {
        this.followerService = followerService
    }

    @PostMapping("/follow")
    ResponseEntity<Void> followUser(@RequestParam String followerId, @RequestParam String followeeId) {
        followerService.followUser(followerId, followeeId)
        ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/unfollow")
    ResponseEntity<Void> unfollowUser(@RequestParam String followerId, @RequestParam String followeeId) {
        followerService.unfollowUser(followerId, followeeId)
        ResponseEntity.noContent().build()
    }

    @GetMapping("/followersByUser/{userId}")
    ResponseEntity<List<FollowerDTO>> getFollowersByUser(@PathVariable String userId) {
        List<FollowerDTO> followers = followerService.getFollowersByUser(userId)
        ResponseEntity.ok(followers)
    }

    @GetMapping("/followeesByUser/{userId}")
    ResponseEntity<List<FollowerDTO>> getFolloweesByUser(@PathVariable String userId) {
        List<FollowerDTO> followees = followerService.getFolloweesByUser(userId)
        ResponseEntity.ok(followees)
    }
}
