package com.dss.carritocompra.utils

import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.Product

object CartManager {
    private val cartItems = mutableListOf<Product>()

    private lateinit var apiService: ApiService

    fun addProduct(product: Product) { cartItems.add(product) }

    fun removeProduct(product: Product) { cartItems.remove(product) }

    fun getCartItems(): List<Product> { return cartItems }

    fun clearCart() {
        cartItems.clear()
    }

}
