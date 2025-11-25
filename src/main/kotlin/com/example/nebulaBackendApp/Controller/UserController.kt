package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Model.LoginRequest
import com.example.nebulaBackendApp.Model.RegisterRequest
import com.example.nebulaBackendApp.Model.UserResponse
import com.example.nebulaBackendApp.Service.UserService
import com.example.nebulaBackendApp.model.User
import com.example.nebulaBackendApp.model.Role
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    // Función de mapeo DTO: Ahora el Long? de this.id coincide con el Long? de UserResponse
    private fun User.toUserResponse(): UserResponse {
        return UserResponse(
            id = this.id,
            name = this.name,
            email = this.email
        )
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<*> {
        return try {
            val newUser = User(
                id = null,
                name = request.name,
                email = request.email,
                password = request.password,
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
    fun loginUser(@RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val userOptional = userService.loginUser(
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