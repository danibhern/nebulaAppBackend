package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Dto.ContactDto
import com.example.nebulaBackendApp.Model.Contact
import com.example.nebulaBackendApp.Repository.ContactRepository

class ContactService(private val contactRepository: ContactRepository) {

    fun getAllContactos(): List<ContactDto> {
        return contactRepository.findAll().map { it.toResponseDto() }

    }

    fun getContactoById(id: Long): ContactDto? {
        return contactRepository.findById(id)
            .map { it.toResponseDto() }
            .orElse(null)
    }

    fun createContacto(createDto: ContactDto): ContactDto {
        // Validación adicional del teléfono si se proporciona
        if (!createDto.telefono.isNullOrBlank()) {
            val telefonoRegex = Regex("^\\d{8,}$")
            if (!createDto.telefono.matches(telefonoRegex)) {
                throw IllegalArgumentException("Teléfono inválido (mínimo 8 dígitos)")
            }
        }

        val contacto = Contact(
            nombre = createDto.nombre,
            email = createDto.email,
            telefono = createDto.telefono,
            asunto = createDto.asunto,
            mensaje = createDto.mensaje
        )

        return contactRepository.save(contacto).toResponseDto()
    }

    fun marcarComoLeido(id: Long): ContactDto? {
        return contactRepository.findById(id)
            .map { contacto ->
                val contactoActualizado = contacto.copy(leido = true)
                contactRepository.save(contactoActualizado).toResponseDto()
            }
            .orElse(null)
    }

    fun getContactosPorEmail(email: String): List<ContactDto> {
        return contactRepository.findByEmail(email).map { it.toResponseDto() }
    }

    fun getContactosNoLeidos(): List<ContactDto> {
        return contactRepository.findByLeido(false).map { it.toResponseDto() }
    }

    fun getContactosLeidos(): List<ContactDto> {
        return contactRepository.findByLeido(true).map { it.toResponseDto() }
    }

    fun contarContactosNoLeidos(): Long {
        return contactRepository.countByLeido(false)
    }

    fun deleteContacto(id: Long): Boolean {
        return if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    private fun Contact.toResponseDto(): ContactDto {
        return ContactDto(
            id = this.id,
            nombre = this.nombre,
            email = this.email,
            telefono = this.telefono,
            asunto = this.asunto,
            mensaje = this.mensaje,
            fechaCreacion = this.fechaCreacion,
            leido = this.leido
        )
    }
}
