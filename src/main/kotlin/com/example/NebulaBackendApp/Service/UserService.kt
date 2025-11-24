package com.example.NebulaBackendApp.Service


import com.example.NebulaBackendApp.Model.User
import com.example.NebulaBackendApp.Repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    // REGISTRO
    fun registerUser(user: User): User {
        if (userRepository.findByEmail(user.email).isPresent) {
            throw RuntimeException("El correo electrónico ya está registrado.")
        }

        // Hashea la contraseña antes de guardarla en la DB
        user.password = passwordEncoder.encode(user.password).toString()

        return userRepository.save(user)
    }

    // LOGIN
    fun loginUser(email: String, rawPassword: String): Optional<User> {
        val userOptional = userRepository.findByEmail(email)

        if (userOptional.isPresent) {
            val user = userOptional.get()
            if (passwordEncoder.matches(rawPassword, user.password)) {
                return Optional.of(user)
            }
        }
        return Optional.empty()
    }
}