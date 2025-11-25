package com.example.nebulaBackendApp.Model

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    // Relación Muchos a Uno con el pedido al que pertenece
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order, // La referencia al Order

    // Relación Muchos a Uno con el producto comprado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: Product, // Asumiendo que ya tienes la entidad Product

    @Column(nullable = false)
    val quantity: Int, // Cantidad de este producto en el pedido

    // Importante: Guardar el precio al momento de la compra
    @Column(nullable = false, precision = 10, scale = 2)
    val unitPrice: BigDecimal
)