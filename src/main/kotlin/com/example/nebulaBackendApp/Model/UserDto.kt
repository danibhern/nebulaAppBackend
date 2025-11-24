package com.example.nebulaBackendApp.Model


data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
)