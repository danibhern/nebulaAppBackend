package com.example.NebulaBackendApp.Service

import com.example.NebulaBackendApp.Model.Product
import com.example.NebulaBackendApp.Repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    // CREATE (C)
    fun createProduct(product: Product): Product {
        return productRepository.save(product)
    }

    // READ ALL (R) - Usado para mostrar el catálogo completo
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    // READ ONE (R)
    fun getProductById(id: Long): Product? {
        return productRepository.findById(id).orElse(null)
    }

    // UPDATE (U)
    fun updateProduct(id: Long, productDetails: Product): Product {
        // Busca el producto existente por ID
        val product = productRepository.findById(id)
            .orElseThrow { RuntimeException("Producto no encontrado con id: $id") }

        // Actualiza los campos con los nuevos detalles
        product.name = productDetails.name
        product.description = productDetails.description
        product.price = productDetails.price
        product.imageUrl = productDetails.imageUrl

        // Guarda y devuelve el producto actualizado
        return productRepository.save(product)
    }

    // DELETE (D)
    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw RuntimeException("Producto no encontrado con id: $id")
        }
        productRepository.deleteById(id)
    }
}