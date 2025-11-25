package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartRepository : JpaRepository<Cart, Long> {

    // Opción 1: Query nativo (más seguro)
    @Query("SELECT c FROM Cart c WHERE c.user.id = :userId")
    fun findCartByUserId(@Param("userId") userId: Long): Optional<Cart>

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    fun findCartByUserEmail(@Param("email") email: String): Optional<Cart>

    // Opción 2: O usar nombres más simples
    fun findByUserId(userId: Long): Optional<Cart>
    fun findByUserEmail(email: String): Optional<Cart>
}