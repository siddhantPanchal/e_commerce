package io.siddhant.e_commerce.repository

import io.siddhant.e_commerce.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : JpaRepository<User, Long> {
    abstract fun findByUsernameIgnoreCase(username: String?): User?
}