package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Model.Product
import com.example.nebulaBackendApp.Model.ProductResponseDTO
import com.example.nebulaBackendApp.Repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

import com.example.nebulaBackendApp.Model.toResponseDTO
@Service
class ProductService(private val productRepository: ProductRepository) {

    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun getProductById(id: Long): Product? {
        return productRepository.findByIdOrNull(id)
    }

    fun createProduct(product: Product): Product {
        return productRepository.save(product.copy(id = 0))
    }

    fun updateProduct(id: Long, productDetails: Product): Product {
        val existingProduct = productRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Producto con ID $id no encontrado para actualizar.")

        val updatedProduct = existingProduct.copy(
            name = productDetails.name,
            description = productDetails.description,
            price = productDetails.price,
            imageUrl = productDetails.imageUrl,
            category = productDetails.category,
            isFavorite = productDetails.isFavorite
        )

        return productRepository.save(updatedProduct)
    }

    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw RuntimeException("Producto con ID $id no encontrado para eliminar.")
        }
        productRepository.deleteById(id)
    }

    fun searchProducts(query: String): List<ProductResponseDTO> {
        return productRepository.findByNameContainingIgnoreCase(query).map { it.toResponseDTO() }
    }

    fun searchProductsByCategory(query: String, category: String): List<ProductResponseDTO> {
        return productRepository.findByCategoryAndNameContainingIgnoreCase(category, query)
            .map { it.toResponseDTO() }
    }

    fun getFavoriteProducts(): List<ProductResponseDTO> {
        return productRepository.findByIsFavorite(true).map { it.toResponseDTO() }
    }

    fun getFavoriteProductsByCategory(category: String): List<ProductResponseDTO> {
        return productRepository.findByIsFavoriteAndCategory(true, category).map { it.toResponseDTO() }
    }

    fun toggleFavorite(id: Long, isFavorite: Boolean): ProductResponseDTO {
        val product = productRepository.findByIdOrNull(id)
            ?: throw RuntimeException("Producto con ID $id no encontrado")

        val updatedProduct = product.copy(isFavorite = isFavorite)
        return productRepository.save(updatedProduct).toResponseDTO()
    }

    fun getProductsSortedByPrice(ascending: Boolean = true): List<ProductResponseDTO> {
        val products = productRepository.findAll()
        val sortedProducts = if (ascending) {
            products.sortedBy { it.price }
        } else {
            products.sortedByDescending { it.price }
        }
        return sortedProducts.map { it.toResponseDTO() }
    }

    fun getProductsByPriceRange(minPrice: Int, maxPrice: Int): List<ProductResponseDTO> {
        return productRepository.findByPriceBetween(minPrice, maxPrice).map { it.toResponseDTO() }
    }

    fun getCategories(): List<String> {
        return productRepository.findDistinctCategories()
    }
}