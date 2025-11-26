package com.example.nebulaBackendApp.Security

import com.example.nebulaBackendApp.Repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username).orElseThrow {
            UsernameNotFoundException("User not found with email: $username")
        }
    }
}