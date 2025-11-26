package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.UserLoginDto
import com.example.nebulaBackendApp.Dto.UserRegisterDto
import com.example.nebulaBackendApp.Model.Role
import com.example.nebulaBackendApp.Model.User
import com.example.nebulaBackendApp.Model.UserResponse
import com.example.nebulaBackendApp.Service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    private fun User.toUserResponse(): UserResponse {
        return UserResponse(
            id = this.id,
            name = this.name,
            email = this.email
        )
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody request: UserRegisterDto): ResponseEntity<*> {
        return try {
            val newUser = User(
                id = null,
                name = request.name,
                email = request.email,
                password = request.password,
                cart = null,
                role = Role.USER
            )

            val registeredUser = userService.registerUser(newUser)
            val responseDto = registeredUser.toUserResponse()

            ResponseEntity(responseDto, HttpStatus.CREATED)
        } catch (e: RuntimeException) {
            // Manejo de errores
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody loginRequest: UserLoginDto): ResponseEntity<*> {
        // Llama al servicio para intentar iniciar sesión
        val userOptional: Optional<User> = userService.loginUser(
            loginRequest.email,
            loginRequest.password
        )

        return if (userOptional.isPresent) {
            val user = userOptional.get()
            val responseDto = user.toUserResponse()
            ResponseEntity.ok(responseDto)
        } else {
            ResponseEntity("Credenciales incorrectas", HttpStatus.UNAUTHORIZED)
        }
    }
}