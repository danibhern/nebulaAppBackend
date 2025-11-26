package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long> {
    fun findByEmail(email: String): List<Contact>
    fun findByLeido(leido: Boolean): List<Contact>
    fun countByLeido(leido: Boolean): Long
}