package com.example.nebulaBackendApp.Model

import com.example.nebulaBackendApp.Model.Cart
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val name: String,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val cart: Cart? = null,

    @Column(unique = true)
    val email: String,

    // CAMBIO 2: Password debe ser 'var' por convención de JPA/posibles updates.
    var password: String,


    @Enumerated(EnumType.STRING)
    val role: Role
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
    }


    override fun getUsername(): String = email
    override fun getPassword(): String = password

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

enum class Role {
    USER, ADMIN
}