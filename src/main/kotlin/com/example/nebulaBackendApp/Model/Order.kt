package com.example.nebulaBackendApp.Model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val orderDate: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<OrderItem> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.PENDING,

    @Column(nullable = false, precision = 10, scale = 2)
    var totalAmount: BigDecimal = BigDecimal.ZERO
)

enum class OrderStatus {
    PENDING,
    PAID,
    SHIPPED,
    COMPLETED,
    CANCELLED
}