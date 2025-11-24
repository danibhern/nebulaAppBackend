package com.example.NebulaBackendApp.Controller

import com.example.NebulaBackendApp.Model.User
import com.example.NebulaBackendApp.Model.LoginRequest
import com.example.NebulaBackendApp.Service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun registerUser(@RequestBody user: User): ResponseEntity<*> {
        return try {
            val registeredUser = userService.registerUser(user)
            registeredUser.password = ""
            ResponseEntity(registeredUser, HttpStatus.CREATED)
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
            user.password = ""
            ResponseEntity.ok(user)
        } else {
            ResponseEntity("Credenciales incorrectas", HttpStatus.UNAUTHORIZED)
        }
    }
}