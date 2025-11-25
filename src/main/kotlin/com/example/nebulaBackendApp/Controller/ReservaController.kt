package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.CreateReservaDto
import com.example.nebulaBackendApp.Model.ReservaResponseDto
import com.example.nebulaBackendApp.Service.ReservaService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/reserva")
class ReservaController (private val reservaService: ReservaService) {

    @GetMapping
    fun getAllReserva():List<ReservaResponseDto>{
        return reservaService.getAllReservas()
    }

    @GetMapping("/{id}")
    fun getReservaById(@PathVariable id: Long):ResponseEntity<ReservaResponseDto>{
        val reserva = reservaService.getReservaById(id)
        return if (reserva!=null){
            ResponseEntity.ok(reserva)
        }else{
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createReserva(@Valid @RequestBody createDto: CreateReservaDto): ResponseEntity<Any> {
        return try {
            val nuevaReserva = reservaService.createReserva(createDto)
            ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateReserva(
        @PathVariable id: Long,
        @Valid @RequestBody updateDto: CreateReservaDto
    ): ResponseEntity<Any> {
        val reservaActualizada = reservaService.updateReserva(id, updateDto)
        return if (reservaActualizada != null) {
            ResponseEntity.ok(reservaActualizada)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteReserva(@PathVariable id: Long): ResponseEntity<Void> {
        return if (reservaService.deleteReserva(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/fecha/{fecha}")
    fun getReservasByFecha(
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) fecha: LocalDate
    ): List<ReservaResponseDto> {
        return reservaService.getReservasByFecha(fecha)
    }

    @GetMapping("/email/{email}")
    fun getReservasByEmail(@PathVariable email: String): List<ReservaResponseDto> {
        return reservaService.getReservasByEmail(email)
    }



}