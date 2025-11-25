package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Contact
import jakarta.validation.constraints.Email
import jdk.incubator.foreign.SymbolLookup
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ContactRepository : JpaRepository<Contact,Long> {

    fun findByEmail(email: String):List<Contact>
    fun findByLeido(leido:Boolean):List<Contact>
    fun findByFechaCreacionBetween(fechaInicio: LocalDateTime, fechaFin: LocalDateTime): List<Contact>
    fun countByLeido(leido: Boolean): Long

}