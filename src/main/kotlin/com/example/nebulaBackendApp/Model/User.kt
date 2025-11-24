package com.example.nebulaBackendApp.Model

import jakarta.persistence.*


@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String,

    @Column(unique = true)
    var email: String,

    var password: String
)