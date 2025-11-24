package com.example.NebulaBackendApp.Model


import jakarta.persistence.*

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    var password: String
) {
    constructor() : this(0, "", "", "")
}

// Objeto para recibir los datos de login del cliente Android
data class LoginRequest(
    val email: String,
    val password: String
)