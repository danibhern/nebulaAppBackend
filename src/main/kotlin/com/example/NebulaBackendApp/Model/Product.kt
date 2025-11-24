package com.example.NebulaBackendApp.Model
import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String?,

    @Column(nullable = false)
    var price: Int,

    @Column(nullable = true)
    var imageUrl: String? = null
) {
    constructor() : this(0, "", null, 0, null)
}