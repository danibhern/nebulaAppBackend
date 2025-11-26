package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Repository.UserRepository
import com.example.nebulaBackendApp.model.User
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
        if (userRepository.findByEmail(user.email).isPresent) {
            throw UserRegistrationException("El email ${user.email} ya está registrado.")
        }

        val hashedPassword = passwordEncoder.encode(user.password)

        val userToSave = user.copy(password = hashedPassword)

        return userRepository.save(userToSave)
    }

    fun loginUser(email: String, rawPassword: String): Optional<User> {
        val userOptional = userRepository.findByEmail(email)

        if (userOptional.isPresent) {
            val user = userOptional.get()

            if (passwordEncoder.matches(rawPassword, user.password)) {
                // Autenticación exitosa
                return userOptional
            }
        }
        return Optional.empty()
    }
}