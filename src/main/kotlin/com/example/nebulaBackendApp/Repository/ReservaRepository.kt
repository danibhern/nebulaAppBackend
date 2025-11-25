package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Reserva
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime

@Repository
interface ReservaRepository: JpaRepository<Reserva,Long>{

    fun findByFecha(fecha:LocalDate):List<Reserva>
    fun findByEmail(email:String):List<Reserva>
    fun existsByFechaAndHora(fecha: LocalDate,hora:LocalTime): Boolean


}