package com.example.nebulaBackendApp.Dto

data class UserRegisterDto(
    // Nombre del usuario (puede ser opcional dependiendo de tus reglas)
    val name: String,
    // Email, que será el identificador único para el login
    val email: String,
    // Contraseña en texto plano antes de ser hasheada en el servicio
    val password: String
)