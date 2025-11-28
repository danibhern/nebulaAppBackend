package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.UserLoginDto
import com.example.nebulaBackendApp.Dto.UserRegisterDto
import com.example.nebulaBackendApp.Model.Role
import com.example.nebulaBackendApp.Model.User
import com.example.nebulaBackendApp.security.JwtService
import com.example.nebulaBackendApp.Service.UserService
import com.example.nebulaBackendApp.Service.UserService.UserRegistrationException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "APIs para gestión de usuarios - registro y autenticación")
class UserController(
    private val userService: UserService,
    private val jwtService: JwtService
) {

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario")
    fun registerUser(
        @RequestBody request: UserRegisterDto
    ): ResponseEntity<*> {
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
            val token = jwtService.generateToken(registeredUser)
            val responseWithToken = mapOf(
                "id" to registeredUser.id,
                "name" to registeredUser.name,
                "email" to registeredUser.email,
                "token" to token
            )
            ResponseEntity(responseWithToken, HttpStatus.CREATED)

        } catch (e: UserRegistrationException) {
            ResponseEntity(mapOf("message" to e.message), HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            ResponseEntity(mapOf("message" to "Error interno del servidor: ${e.message}"), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    fun loginUser(
        @RequestBody loginRequest: UserLoginDto
    ): ResponseEntity<*> {
        val userOptional: Optional<User> = userService.loginUser(
            loginRequest.email,
            loginRequest.password
        )

        return if (userOptional.isPresent) {
            val user = userOptional.get()
            val token = jwtService.generateToken(user)
            val responseWithToken = mapOf(
                "id" to user.id,
                "name" to user.name,
                "email" to user.email,
                "token" to token
            )
            ResponseEntity.ok(responseWithToken)
        } else {
            ResponseEntity(mapOf("message" to "Credenciales incorrectas"), HttpStatus.UNAUTHORIZED)
        }
    }
}
