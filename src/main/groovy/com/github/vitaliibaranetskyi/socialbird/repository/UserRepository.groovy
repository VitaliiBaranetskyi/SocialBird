package com.github.vitaliibaranetskyi.socialbird.repository

import com.github.vitaliibaranetskyi.socialbird.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<User, String>{
    Optional<User> findByUsername(String username)
    boolean existsByUsername(String username)
}