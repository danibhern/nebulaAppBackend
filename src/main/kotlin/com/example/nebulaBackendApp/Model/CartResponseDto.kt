package com.example.nebulaBackendApp.Dto

import com.example.nebulaBackendApp.Model.ProductResponseDTO
import jakarta.validation.constraints.*

data class CartResponseDTO(
    val id: Long,
    val items: List<CartItemResponseDTO>,
    val subtotal: Int,
    val shippingMessage: String,
    val grandTotal: Int
)

data class CartItemResponseDTO(
    val id: Long,
    val product: ProductResponseDTO,
    val quantity: Int
)

data class AddToCartRequest(
    @field:NotNull(message = "El ID del producto es obligatorio")
    val productId: Long,

    @field:NotNull(message = "La cantidad es obligatoria")
    @field:Min(value = 1, message = "La cantidad debe ser al menos 1")
    val quantity: Int = 1
)

data class UpdateCartItemRequest(
    @field:NotNull(message = "La cantidad es obligatoria")
    @field:Min(value = 1, message = "La cantidad debe ser al menos 1")
    val quantity: Int
)