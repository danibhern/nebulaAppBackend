package com.example.nebulaBackendApp.Model

import com.example.nebulaBackendApp.model.User
import jakarta.persistence.*

@Entity
@Table(name = "cart")

data class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    val items: MutableList<CartItem> = mutableListOf(),

    @Column(nullable = false)
    var subtotal: Int = 0,

    @Column(nullable = false)
    var shippingMessage: String = "Por Pagar",

    @Column(nullable = false)
    var grandTotal: Int = 0

)
