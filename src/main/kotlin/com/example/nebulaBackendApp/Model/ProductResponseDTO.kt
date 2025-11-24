package com.example.nebulaBackendApp.Model

// DTO: Objeto de Transferencia de Datos que define la estructura de la respuesta JSON al cliente.
// No contiene anotaciones JPA, solo los campos que necesitamos exponer.
data class ProductResponseDTO(
    // ID como String para mayor flexibilidad
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