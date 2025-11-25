package com.example.nebulaBackendApp.Dto

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class ContactDto(
    val id: Long = 0,

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    val nombre: String,

    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El formato del email no es válido")
    val email: String,

    val telefono: String? = null,

    @field:NotBlank(message = "El asunto es obligatorio")
    @field:Size(min = 5, message = "El asunto debe tener al menos 5 caracteres")
    val asunto: String,

    @field:NotBlank(message = "El mensaje es obligatorio")
    @field:Size(min = 10, message = "El mensaje debe tener al menos 10 caracteres")
    val mensaje: String,

    val fechaCreacion: LocalDateTime? = null,
    val leido: Boolean = false
)