package com.example.nebulaBackendApp.Model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

// Importación para usar una marca de tiempo automática si lo deseas
import org.hibernate.annotations.CreationTimestamp

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

    // CORRECCIÓN 1: Si 'telefono' es opcional (como en el DTO), debe ser nullable en la DB
    @Column(nullable = true)
    val telefono:String? = null,

    @Column(nullable = false)
    val mensaje:String,

    @Column(nullable = false)
    val asunto:String,

    // CORRECCIÓN 2: Usar @CreationTimestamp para que JPA/Hibernate lo maneje,
    // y permitir que el Contacto se construya sin tener que pasarlo.
    @CreationTimestamp // Se llena automáticamente al persistir el objeto
    @Column(nullable = false, length = 1000)
    val fechaCreacion :LocalDateTime = LocalDateTime.now(),

    // CORRECCIÓN 3: 'leido' lo establecemos en el constructor por defecto.
    @Column(nullable = false)
    val leido: Boolean = false
)