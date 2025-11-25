package com.example.nebulaBackendApp.Dto

import jakarta.validation.constraints.*
import java.time.LocalDate
import java.time.LocalTime

data class CreateReservaDto(
    @field:NotBlank(message = "El nombre es obligatorio")
    val nameCliente: String,

    @field:NotBlank(message = "EL email es obligatorio")
    @field:Email(message = "Elformato no es valido")
    val email :String,

    @field:NotBlank(message = "La fecha es obligatoria")
    val fecha :LocalDate,

    @field:NotBlank(message = "La hora es obligatoria")
    val hora :LocalTime,

    @field:Min(1, message = "Debe haber al menos 1 persona")
     val numPersonas: Int

) {

}

