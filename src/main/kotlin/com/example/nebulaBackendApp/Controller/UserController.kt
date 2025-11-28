package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Dto.UserLoginDto
import com.example.nebulaBackendApp.Dto.UserRegisterDto
import com.example.nebulaBackendApp.Model.Role
import com.example.nebulaBackendApp.Model.User
import com.example.nebulaBackendApp.Model.UserResponse
import com.example.nebulaBackendApp.Service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "APIs para gestión de usuarios - registro y autenticación")
class UserController(private val userService: UserService) {

    private fun User.toUserResponse(): UserResponse {
        return UserResponse(
            id = this.id,
            name = this.name,
            email = this.email
        )
    }

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea una nueva cuenta de usuario en el sistema"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Usuario registrado exitosamente"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Datos de registro inválidos o usuario ya existe"
            )
        ]
    )
    fun registerUser(
        @Parameter(
            description = "Datos para registro de nuevo usuario",
            required = true
        )
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
            val responseDto = registeredUser.toUserResponse()

            ResponseEntity(responseDto, HttpStatus.CREATED)
        } catch (e: RuntimeException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Login exitoso, retorna datos del usuario"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Credenciales incorrectas"
            )
        ]
    )
    fun loginUser(
        @Parameter(
            description = "Credenciales de acceso",
            required = true
        )
        @RequestBody loginRequest: UserLoginDto
    ): ResponseEntity<*> {
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