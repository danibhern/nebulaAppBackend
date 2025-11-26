package com.example.nebulaBackendApp.Service

import com.example.nebulaBackendApp.Dto.*
import com.example.nebulaBackendApp.Model.*
import com.example.nebulaBackendApp.Repository.CartRepository
import com.example.nebulaBackendApp.Repository.ProductRepository
import com.example.nebulaBackendApp.Repository.UserRepository
import com.example.nebulaBackendApp.Model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CartService(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {

    private val shippingThreshold = 29990


    // CAMBIAR: findByUserEmail -> findByUser_Email
    fun getCartByUserEmail(email: String): CartResponseDTO {
        val cart = cartRepository.findByUserEmail(email)
            .orElseGet {
                val user = userRepository.findByEmail(email)
                    .orElseThrow { RuntimeException("Usuario no encontrado") }
                createCartForUser(user)
            }
        return convertToCartResponseDTO(cart)
    }

    // CAMBIAR TODOS los findByUserId -> findByUser_Id
    fun updateCartItem(userId: Long, itemId: Long, request: UpdateCartItemRequest): CartResponseDTO {
        val cart = cartRepository.findByUserId(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado") }

        val cartItem = cart.items.find { it.id == itemId }
            ?: throw RuntimeException("Item del carrito no encontrado")

        cartItem.quantity = request.quantity
        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun removeFromCart(userId: Long, productId: Long): CartResponseDTO {
        val cart = cartRepository.findByUserId(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado") }

        val existingItem = cart.items.find { it.product.id == productId }
            ?: throw RuntimeException("Producto no encontrado en el carrito")

        if (existingItem.quantity > 1) {
            existingItem.quantity -= 1
        } else {
            cart.items.remove(existingItem)
        }

        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun deleteItemFromCart(userId: Long, itemId: Long): CartResponseDTO {
        val cart = cartRepository.findByUserId(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado") }

        cart.items.removeIf { it.id == itemId }
        updateCartCalculations(cart)
        val savedCart = cartRepository.save(cart)
        return convertToCartResponseDTO(savedCart)
    }

    fun clearCart(userId: Long): CartResponseDTO {
        val cart = cartRepository.findByUserId(userId)
            .orElseThrow { RuntimeException("Carrito no encontrado") }

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
            "Gratis" to 0
        } else {
            "Por Pagar" to 0
        }

        cart.subtotal = subtotal
        cart.shippingMessage = shippingMessage
        cart.grandTotal = subtotal + shippingCost
    }

    // Funciones de conversión
    private fun convertToCartResponseDTO(cart: Cart): CartResponseDTO {
        return CartResponseDTO(
            id = cart.id,
            items = cart.items.map { convertToCartItemResponseDTO(it) },
            subtotal = cart.subtotal,
            shippingMessage = cart.shippingMessage,
            grandTotal = cart.grandTotal
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