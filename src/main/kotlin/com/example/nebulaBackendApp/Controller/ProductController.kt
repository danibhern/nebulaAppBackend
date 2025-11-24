package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Model.Product
import com.example.nebulaBackendApp.Service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun getAllProducts(): List<Product> {
        return productService.getAllProducts()
    }
    @PostMapping
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val newProduct = productService.createProduct(product)
        return ResponseEntity(newProduct, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return product?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    fun updateProduct(@PathVariable id: Long, @RequestBody productDetails: Product): ResponseEntity<*> {
        return try {
            val updatedProduct = productService.updateProduct(id, productDetails)
            ResponseEntity.ok(updatedProduct)
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