package com.example.nebulaBackendApp.Model

import jakarta.persistence.*
import java.util.UUID // <--- ¡Esta importación es crucial!

/**
 * Entidad que representa a un usuario en la base de datos.
 * El 'id' se define como UUID y es la clave primaria.
 */
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    val name: String,

    @Column(unique = true)
    val email: String,

    val password: String
)