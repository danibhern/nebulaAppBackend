package com.example.NebulaBackendApp.Controller

import com.example.NebulaBackendApp.Model.Product
import com.example.NebulaBackendApp.Model.ProductResponseDTO
import com.example.NebulaBackendApp.Model.toResponseDTO
import com.example.NebulaBackendApp.Service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<ProductResponseDTO> {
        return productService.getAllProducts()
            .map { it.toResponseDTO() }
    }

    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<ProductResponseDTO> {
        val newProduct = productService.createProduct(product)
        return ResponseEntity(newProduct.toResponseDTO(), HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<ProductResponseDTO> {
        val product = productService.getProductById(id)
        return product?.let {
            ResponseEntity.ok(it.toResponseDTO())
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody productDetails: Product): ResponseEntity<*> {
        return try {
            val updatedProduct = productService.updateProduct(id, productDetails)
            ResponseEntity.ok(updatedProduct.toResponseDTO())
        } catch (e: RuntimeException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            productService.deleteProduct(id)
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}