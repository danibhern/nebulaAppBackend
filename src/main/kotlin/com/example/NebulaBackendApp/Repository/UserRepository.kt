package com.example.NebulaBackendApp.Repository


import com.example.NebulaBackendApp.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}