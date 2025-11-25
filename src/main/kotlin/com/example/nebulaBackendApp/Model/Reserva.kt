package com.example.nebulaBackendApp.Model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name ="reserva")
data class Reserva (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id :Long = 0,

    @Column(nullable = false)
    val nameCliente: String,

    @Column(nullable = false)
    val email :String,

    @Column(nullable = false)
    val fecha : LocalDate,

    @Column(nullable = false)
    val hora : LocalTime,

    
)