package com.example.NebulaBackendApp.Model

data class ProductResponseDTO(
    val id: String,
    val name: String,
    val description: String?,
    val price: Int,
    val imageUrl: String
)

fun Product.toResponseDTO(): ProductResponseDTO {
    return ProductResponseDTO(
        id = this.id.toString(),
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl ?: ""
    )
}