package com.example.nebulaBackendApp.Security

import io.jsonwebtoken.Claims // Debe ser esta
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys // Importación correcta para Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

    @Value("\${jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${jwt.expiration-time}")
    private val expirationTime: Long = 86400000

    private val signingKey: SecretKey
        get() = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = mapOf("roles" to userDetails.authorities.map { it.authority })
        return buildToken(claims, userDetails)
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails
    ): String {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username) // El sujeto (subject) es el identificador del usuario (email o username)
            .issuedAt(Date(System.currentTimeMillis())) // Fecha de emisión
            .expiration(Date(System.currentTimeMillis() + expirationTime)) // Fecha de expiración
            .signWith(signingKey, Jwts.SIG.HS256) // Firma con la clave y algoritmo HS256
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}