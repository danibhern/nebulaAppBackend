package com.example.nebulaBackendApp.Model

/**
 * Representa el cuerpo de la petición (JSON) para iniciar sesión.
 */
data class LoginRequest(
    val email: String,
    val password: String
)