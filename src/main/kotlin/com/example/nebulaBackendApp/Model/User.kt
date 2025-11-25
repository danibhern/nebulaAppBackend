package com.example.nebulaBackendApp.Model

import jakarta.persistence.*
import java.util.UUID

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