package com.example.nebulaBackendApp.Model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name="contacto")
data class Contact(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id :Long = 0,

    @Column(nullable = false)
    val nombre :String,

    @Column(nullable = false)
    val email :String,

    @Column(nullable = false)
    val telefono:String? = null,

    @Column(nullable = false)
    val mensaje:String,

    @Column(nullable = false)
    val asunto:String,

    @Column(nullable = false,length=1000)
    val fechaCreacion :LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val leido: Boolean = false
)