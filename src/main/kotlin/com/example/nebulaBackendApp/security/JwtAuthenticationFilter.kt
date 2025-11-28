package com.example.nebulaBackendApp.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    @Qualifier("userDetailServiceImpl")
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    // =================================================================
    // ✅ CORRECCIÓN: Método para que el filtro omita la ruta de H2 Console.
    // Retorna 'true' si el filtro NO DEBE ejecutarse (lo ignoramos).
    // =================================================================
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        // Ignora cualquier ruta que empiece con /h2-console
        return request.getRequestURI().startsWith("/h2-console")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Nota: Debido a shouldNotFilter, esta lógica solo se ejecuta para
        // peticiones que NO van a /h2-console.

        val authHeader: String? = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        val username = jwtService.extractUsername(jwt)

        if (username != null && SecurityContextHolder.getContext().authentication == null) {

            val userDetails = this.userDetailsService.loadUserByUsername(username)

            if (jwtService.isTokenValid(jwt, userDetails)) {

                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }
}