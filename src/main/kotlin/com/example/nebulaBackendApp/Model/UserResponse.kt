package com.example.nebulaBackendApp.Model

import java.util.UUID

data class UserResponse(
    val id: UUID?,
    val name: String,
    val email: String
)
