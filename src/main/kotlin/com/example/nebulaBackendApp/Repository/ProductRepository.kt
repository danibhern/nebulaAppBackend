package com.example.nebulaBackendApp.Repository

import com.example.nebulaBackendApp.Model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    //busca producto por nombre
    fun findByNameContainingIgnoreCase(name:String):List<Product>

    //busca producto por rango de precio
    fun findByPriceBetween(minPrice:Int , maxPrice :Int): List<Product>

    // Buscar por categoría y nombre
    fun findByCategoryAndNameContainingIgnoreCase(category: String, name: String): List<Product>

    // Buscar favoritos
    fun findByIsFavorite(isFavorite: Boolean): List<Product>

    @Query("SELECT DISTINCT p.category FROM Product p")
    fun findDistinctCategories(): List<String>

    // Buscar favoritos por categoría
    fun findByIsFavoriteAndCategory(isFavorite: Boolean, category: String): List<Product>
}