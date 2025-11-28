package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.*
import com.example.nebulaBackendApp.Service.CartService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart", description = "APIs para gestión del carrito de compras")
class CartController(private val cartService: CartService) {

    @GetMapping("/user/email/{email}")
    @Operation(
        summary = "Obtener carrito por email",
        description = "Retorna el carrito de compras asociado a un email de usuario"
    )
    fun getCartByUserEmail(@PathVariable email: String): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.getCartByUserEmail(email)
            ResponseEntity.ok(cart)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PutMapping("/user/{userId}/items/{itemId}")
    @Operation(
        summary = "Actualizar item del carrito",
        description = "Actualiza la cantidad de un producto específico en el carrito"
    )
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
    @Operation(
        summary = "Remover producto del carrito",
        description = "Reduce o remueve un producto del carrito de compras"
    )
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
    @Operation(
        summary = "Eliminar item del carrito",
        description = "Elimina completamente un item del carrito de compras"
    )
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
    @Operation(
        summary = "Limpiar carrito",
        description = "Elimina todos los items del carrito de compras"
    )
    fun clearCart(@PathVariable userId: Long): ResponseEntity<CartResponseDTO> {
        return try {
            val cart = cartService.clearCart(userId)
            ResponseEntity.ok(cart)
        } catch (e: RuntimeException) {
            ResponseEntity.notFound().build()
        }
    }
}