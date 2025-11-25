package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.*
import com.example.nebulaBackendApp.Service.CartService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
class CartController(private val cartService: CartService) {



    @GetMapping("/user/email/{email}")
    fun getCartByUserEmail(@PathVariable email: String): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.getCartByUserEmail(email)
            ResponseEntity.ok(cart)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PutMapping("/user/{userId}/items/{itemId}")
    fun updateCartItem(
        @PathVariable userId: Long,
        @PathVariable itemId: Long,
        @Valid @RequestBody request: UpdateCartItemRequest
    ): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.updateCartItem(userId, itemId, request)
            ResponseEntity.ok(cart)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/user/{userId}/items/{productId}/remove")
    fun removeFromCart(
        @PathVariable userId: Long,
        @PathVariable productId: Long
    ): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.removeFromCart(userId, productId)
            ResponseEntity.ok(cart)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/user/{userId}/items/{itemId}")
    fun deleteItemFromCart(
        @PathVariable userId: Long,
        @PathVariable itemId: Long
    ): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.deleteItemFromCart(userId, itemId)
            ResponseEntity.ok(cart)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/user/{userId}/clear")
    fun clearCart(@PathVariable userId: Long): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.clearCart(userId)
            ResponseEntity.ok(cart)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }
}