package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Model.Product
import com.example.nebulaBackendApp.Repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.data.repository.findByIdOrNull

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
            imageUrl = productDetails.imageUrl
        )

        return productRepository.save(updatedProduct)
    }

    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw RuntimeException("Producto con ID $id no encontrado para eliminar.")
        }
        productRepository.deleteById(id)
    }
}