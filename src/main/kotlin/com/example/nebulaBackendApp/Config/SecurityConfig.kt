package com.example.nebulaBackendApp.config

import com.example.nebulaBackendApp.Security.UserDetailServiceImpl
import com.example.nebulaBackendApp.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.List

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailServiceImpl: UserDetailServiceImpl,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { cors -> cors.configurationSource(corsConfigurationSource()) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                    .anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            // 2. Necesario para que h2-console funcione en un iframe
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }
            }
            // 3. Añadir nuestro filtro JWT ANTES del filtro de usuario/contraseña de Spring
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOriginPatterns = List.of("*")

        configuration.allowedMethods = List.of("*")
        configuration.allowedHeaders = List.of("*")
        configuration.exposedHeaders = List.of("Authorization", "Content-Type")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailServiceImpl)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}