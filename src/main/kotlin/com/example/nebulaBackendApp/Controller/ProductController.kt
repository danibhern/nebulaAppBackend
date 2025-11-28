package com.example.nebulaBackendApp.Controller

import com.example.nebulaBackendApp.Model.Product
import com.example.nebulaBackendApp.Service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "APIs para gestión de productos")
class ProductController(private val productService: ProductService) {

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna la lista completa de productos")
    fun getAllProducts(): List<Product> {
        return productService.getAllProducts()
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    fun createProduct(@RequestBody product: Product): ResponseEntity<Product> {
        val newProduct = productService.createProduct(product)
        return ResponseEntity(newProduct, HttpStatus.CREATED)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto específico por su ID")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return product?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza la información de un producto existente")
    fun updateProduct(@PathVariable id: Long, @RequestBody productDetails: Product): ResponseEntity<*> {
        return try {
            val updatedProduct = productService.updateProduct(id, productDetails)
            ResponseEntity.ok(updatedProduct)
        } catch (e: RuntimeException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema por su ID")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        return try {
            productService.deleteProduct(id)
            ResponseEntity<Void>(HttpStatus.NO_CONTENT)
        } catch (e: RuntimeException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}