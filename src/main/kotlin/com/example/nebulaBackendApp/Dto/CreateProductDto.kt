package com.example.nebulaBackendApp.Dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CreateProductDto(
    @field:NotBlank(message = "El nombre es obligatorio")
    val name: String,

    val description: String? = null,

    @field:NotNull(message = "El precio es obligatorio")
    @field:Positive(message = "El precio debe ser mayor a 0")
    val price: Int,

    val imageUrl: String? = null,

    @field:NotBlank(message = "La categoría es obligatoria")
    val category: String, // "COFFEE" o "ACCESSORIES"

    val isFavorite: Boolean = false
)

