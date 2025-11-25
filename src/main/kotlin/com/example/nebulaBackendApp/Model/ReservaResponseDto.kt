package com.example.nebulaBackendApp.Model

import java.time.LocalDate
import java.time.LocalTime

data class ReservaResponseDto(
    val id: Long,
    val nameCliente: String,
    val email: String,
    val fecha: LocalDate,
    val hora: LocalTime,
    val numPersonas: Int
)