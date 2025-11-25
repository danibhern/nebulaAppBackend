package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Dto.CreateReservaDto
import com.example.nebulaBackendApp.Model.Reserva
import com.example.nebulaBackendApp.Model.ReservaResponseDto
import com.example.nebulaBackendApp.Repository.ReservaRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReservaService (private val reservaRepository: ReservaRepository) {

    fun getAllReservas(): List<ReservaResponseDto> {
        return reservaRepository.findAll().map { it.toResponseDto() }
    }

    private fun Reserva.toResponseDto(): ReservaResponseDto {
        return ReservaResponseDto(
            id = this.id,
            nameCliente = this.nameCliente,
            email = this.email,
            fecha = this.fecha,
            hora = this.hora,
            numPersonas = this.numPersonas
        )
    }

    fun getReservaById(id: Long): ReservaResponseDto? {
        return reservaRepository.findById(id)
            .map { it.toResponseDto() }
            .orElse(null)
    }

    fun createReserva(createDto: CreateReservaDto): ReservaResponseDto {
        // Validar que no exista reserva en misma fecha y hora
        if (reservaRepository.existsByFechaAndHora(createDto.fecha, createDto.hora)) {
            throw IllegalArgumentException("Ya existe una reserva para esta fecha y hora")
        }

        val reserva = Reserva(
            nameCliente = createDto.nameCliente,
            email = createDto.email,
            fecha = createDto.fecha,
            hora = createDto.hora,
            numPersonas = createDto.numPersonas
        )

        return reservaRepository.save(reserva).toResponseDto()
    }

    fun updateReserva(id: Long, updateDto: CreateReservaDto): ReservaResponseDto? {
        return reservaRepository.findById(id)
            .map { existingReserva ->
                val updatedReserva = existingReserva.copy(
                    nameCliente = updateDto.nameCliente,
                    email = updateDto.email,
                    fecha = updateDto.fecha,
                    hora = updateDto.hora,
                    numPersonas = updateDto.numPersonas
                )
                reservaRepository.save(updatedReserva).toResponseDto()
            }
            .orElse(null)
    }

    fun deleteReserva(id: Long): Boolean {
        return if (reservaRepository.existsById(id)) {
            reservaRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun getReservasByFecha(fecha: LocalDate): List<ReservaResponseDto> {
        return reservaRepository.findByFecha(fecha).map { it.toResponseDto() }
    }

    fun getReservasByEmail(email: String): List<ReservaResponseDto> {
        return reservaRepository.findByEmail(email).map { it.toResponseDto() }
    }
}