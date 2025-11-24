package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Dto.UserLoginDto
import com.example.nebulaBackendApp.Dto.UserRegisterDto
import com.example.nebulaBackendApp.Model.User
import com.example.nebulaBackendApp.Repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(dto: UserRegisterDto): User? {
        if (userRepository.findByEmail(dto.email).isPresent) {
            // El usuario ya existe
            return null
        }

        val newUser = User(
            name = dto.name,
            email = dto.email,
            password = passwordEncoder.encode(dto.password)
        )

        return userRepository.save(newUser)
    }

    fun findByEmail(email: String): User? {
        // Usar el método findByEmail del repositorio
        return userRepository.findByEmail(email).orElse(null)
    }

    fun login(dto: UserLoginDto): User? {
        val user = findByEmail(dto.email) ?: return null

        if (passwordEncoder.matches(dto.password, user.password)) {
            return user
        }
        return null
    }
}