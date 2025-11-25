package org.springframework.security.core.userdetails

interface UserDetailsService {
    fun loadUserByUsername(username: String): UserDetails
}