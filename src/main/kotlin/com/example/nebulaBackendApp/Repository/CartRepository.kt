package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartRepository : JpaRepository<Cart, Long> {

    fun findByUser_Id(userId: Long): Optional<Cart>
    fun findByUser_Email(email: String): Optional<Cart>
}