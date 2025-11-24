package com.example.nebulaBackendApp.Dto

data class UserLoginDto(
    // Email del usuario
    val email: String,
    // Contraseña ingresada por el usuario
    val password: String
)