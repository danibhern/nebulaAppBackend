package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository

interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): Optional<User>
}