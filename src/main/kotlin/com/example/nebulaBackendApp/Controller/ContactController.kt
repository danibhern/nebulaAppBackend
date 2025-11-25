package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.ContactDto
import com.example.nebulaBackendApp.Service.ContactService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/contact")
class ContactController(private val contactService: ContactService) {

    @GetMapping
    fun getAllContactos(): List<ContactDto> {
        return contactService.getAllContactos()
    }

    // CORRECCIÓN: Cambiado a @GetMapping con path y agregado @PathVariable
    @GetMapping("/{id}")
    fun getContactoById(@PathVariable id: Long): ResponseEntity<ContactDto> {
        // CORRECCIÓN: Cambiado "," por "."
        val contacto = contactService.getContactoById(id)
        return if (contacto != null) {
            ResponseEntity.ok(contacto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createContacto(@Valid @RequestBody createDto: ContactDto): ResponseEntity<Any> { // CORRECCIÓN: "creteDto" -> "createDto"
        return try {
            val nuevoContacto = contactService.createContacto(createDto)
            ResponseEntity.status(HttpStatus.CREATED).body(nuevoContacto)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}/leido")
    fun marcarComoLeido(@PathVariable id: Long): ResponseEntity<ContactDto> {
        val contacto = contactService.marcarComoLeido(id)
        return if (contacto != null) {
            ResponseEntity.ok(contacto)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/email/{email}")
    fun getContactosPorEmail(@PathVariable email: String): List<ContactDto> {
        return contactService.getContactosPorEmail(email)
    }

    @GetMapping("/no-leidos")
    fun getContactosNoLeidos(): List<ContactDto> {
        return contactService.getContactosNoLeidos()
    }

    @GetMapping("/leidos")
    fun getContactosLeidos(): List<ContactDto> {
        return contactService.getContactosLeidos()
    }

    @GetMapping("/contador/no-leidos")
    fun contarContactosNoLeidos(): Map<String, Long> {
        return mapOf("totalNoLeidos" to contactService.contarContactosNoLeidos())
    }

    @DeleteMapping("/{id}")
    fun deleteContacto(@PathVariable id: Long): ResponseEntity<Void> {
        return if (contactService.deleteContacto(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}