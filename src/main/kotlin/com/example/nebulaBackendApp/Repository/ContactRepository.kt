package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Contact
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ContactRepository : JpaRepository<Contact, Long> {
    fun findByEmail(email: String): Optional<Contact>
}