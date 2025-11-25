package com.example.nebulaBackendApp.Model

data class UserResponse(
    val id: Long?, // <-- TIPO DE ID CORREGIDO
    val name: String,
    val email: String
)