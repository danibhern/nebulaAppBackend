package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Model.User
import com.example.nebulaBackendApp.Repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    class UserRegistrationException(message: String) : RuntimeException(message)

    fun registerUser(user: User): User {
        // 1. Verificar si el email ya existe
        if (userRepository.findByEmail(user.email).isPresent) {
            throw UserRegistrationException("El email ${user.email} ya está registrado.")
        }

        // 2. Hashear la contraseña que viene del controlador antes de guardar
        val hashedPassword = passwordEncoder.encode(user.password)

        // Crea una nueva entidad con la contraseña hasheada, preservando el resto de los datos
        val userToSave = user.copy(password = hashedPassword)

        // 3. Guardar en la base de datos
        return userRepository.save(userToSave)
    }

    fun loginUser(email: String, rawPassword: String): Optional<User> {
        // 1. Buscar el usuario por email
        val userOptional = userRepository.findByEmail(email)

        if (userOptional.isPresent) {
            val user = userOptional.get()

            // 2. Verificar la contraseña hasheada
            if (passwordEncoder.matches(rawPassword, user.password)) {
                // Autenticación exitosa
                return userOptional
            }
        }

        // 3. Autenticación fallida (usuario no encontrado o contraseña incorrecta)
        return Optional.empty()
    }

    // NOTA: Eliminar o renombrar 'register' y 'login' si existen para evitar ambigüedades.
}