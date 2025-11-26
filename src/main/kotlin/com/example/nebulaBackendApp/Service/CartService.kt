package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Dto.*
import com.example.nebulaBackendApp.Model.*
import com.example.nebulaBackendApp.Repository.CartRepository
import com.example.nebulaBackendApp.Repository.ProductRepository
import com.example.nebulaBackendApp.Repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CartService(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {

    // Define el umbral de envío gratuito (e.g., $29,990)
    private val shippingThreshold = 29990
    private val defaultShippingCost = 5000 // Costo de envío por defecto

    fun getCartByUserEmail(email: String): CartResponseDTO {
        val cart = cartRepository.findByUser_Email(email) // CORRECCIÓN: findByUser_Email
            .orElseGet {
                val user = userRepository.findByEmail(email)
                    .orElseThrow { RuntimeException("Usuario no encontrado con email: $email") }
                createCartForUser(user)
            }
        return convertToCartResponseDTO(cart)
    }

    fun addToCart(email: String, productId: Long): CartResponseDTO {
        val cart = cartRepository.findByUser_Email(email) // CORRECCIÓN: findByUser_Email
            .orElseGet {
                val user = userRepository.findByEmail(email)
                    .orElseThrow { RuntimeException("Usuario no encontrado con email: $email") }
                createCartForUser(user)
            }

        val product = productRepository.findById(productId)
            .orElseThrow { RuntimeException("Producto no encontrado con ID: $productId") }

        // Buscar si el producto ya está en el carrito
        val existingItem = cart.items.find { it.product.id == productId }

        if (existingItem != null) {
            // Si existe, incrementar la cantidad
            existingItem.quantity += 1
        } else {
            // Si no existe, crear un nuevo CartItem
            val newItem = CartItem(cart = cart, product = product, quantity = 1)
            cart.items.add(newItem)
        }

        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun updateCartItem(userId: Long, itemId: Long, request: UpdateCartItemRequest): CartResponseDTO {
        // CORRECCIÓN: findByUser_Id
        val cart = cartRepository.findByUser_Id(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado para el usuario ID: $userId") }

        val cartItem = cart.items.find { it.id == itemId }
            ?: throw RuntimeException("Item del carrito ID: $itemId no encontrado")

        if (request.quantity <= 0) {
            // Si la cantidad es 0 o menos, eliminar el ítem
            cart.items.remove(cartItem)
        } else {
            cartItem.quantity = request.quantity
        }

        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun removeFromCart(userId: Long, productId: Long): CartResponseDTO {
        // CORRECCIÓN: findByUser_Id
        val cart = cartRepository.findByUser_Id(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado para el usuario ID: $userId") }

        val existingItem = cart.items.find { it.product.id == productId }
            ?: throw RuntimeException("Producto ID: $productId no encontrado en el carrito")

        if (existingItem.quantity > 1) {
            existingItem.quantity -= 1
        } else {
            // Si solo queda 1, lo elimina de la colección (gracias a orphanRemoval, esto lo borra de la DB)
            cart.items.remove(existingItem)
        }

        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun deleteItemFromCart(userId: Long, itemId: Long): CartResponseDTO {
        // CORRECCIÓN: findByUser_Id
        val cart = cartRepository.findByUser_Id(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado para el usuario ID: $userId") }

        val removed = cart.items.removeIf { it.id == itemId }
        if (!removed) throw RuntimeException("Item del carrito ID: $itemId no encontrado para eliminar.")

        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun clearCart(userId: Long): CartResponseDTO {
        // CORRECCIÓN: findByUser_Id
        val cart = cartRepository.findByUser_Id(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado para el usuario ID: $userId") }

        cart.items.clear()
        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    private fun createCartForUser(user: User): Cart {
        val cart = Cart(user = user)
        return cartRepository.save(cart)
    }

    private fun updateCartCalculations(cart: Cart) {
        val subtotal = cart.items.sumOf { it.product.price * it.quantity }

        val (shippingMessage, shippingCost) = if (subtotal >= shippingThreshold) {
            "Envío Gratis" to 0
        } else if (subtotal > 0) {
            "Por Pagar ($${defaultShippingCost})" to defaultShippingCost
        } else {
            "Carrito Vacío" to 0
        }

    }

    private fun convertToCartResponseDTO(cart: Cart): CartResponseDTO {
        val subtotalValue = cart.items.sumOf { it.product.price * it.quantity }
        val grandTotalValue = subtotalValue // Simplificado

        return CartResponseDTO(
            id = cart.id,
            items = cart.items.map { convertToCartItemResponseDTO(it) },
            subtotal = subtotalValue, // Debe venir del campo de la entidad Cart
            shippingMessage = "Calculado en la conversión", // Debe venir del campo de la entidad Cart
            grandTotal = grandTotalValue // Debe venir del campo de la entidad Cart
        )
    }

    private fun convertToCartItemResponseDTO(cartItem: CartItem): CartItemResponseDTO {
        return CartItemResponseDTO(
            id = cartItem.id,
            product = convertToProductResponseDTO(cartItem.product),
            quantity = cartItem.quantity
        )
    }

    private fun convertToProductResponseDTO(product: Product): ProductResponseDTO {
        return ProductResponseDTO(
            id = product.id.toString(),
            name = product.name,
            description = product.description,
            price = product.price,
            imageUrl = product.imageUrl,
            category = product.category,
            isFavorite = product.isFavorite
        )
    }
}