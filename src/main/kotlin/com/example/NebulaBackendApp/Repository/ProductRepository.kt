package com.example.NebulaBackendApp.Repository

import com.example.NebulaBackendApp.Model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    // Aquí podrías agregar métodos de consulta personalizados si la app los necesita,
    // por ejemplo, para buscar productos por nombre o categoría.
    // fun findByNameContainingIgnoreCase(name: String): List<Product>
}