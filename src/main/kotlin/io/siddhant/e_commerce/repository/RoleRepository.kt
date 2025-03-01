package io.siddhant.e_commerce.repository

import io.siddhant.e_commerce.domain.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Role?
}