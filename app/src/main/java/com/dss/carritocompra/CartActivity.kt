package com.dss.carritocompra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.ProductAdapter
import com.dss.carritocompra.utils.CartManager

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val cartProducts = CartManager.getCartItems().toMutableList()
        productAdapter = ProductAdapter(cartProducts) { product ->
            CartManager.removeProduct(product)
            productAdapter.removeProduct(product)
        }
        recyclerView.adapter = productAdapter

        val buttonCheckout: Button = findViewById(R.id.buttonCheckout)
        buttonCheckout.setOnClickListener {
            if (CartManager.getCartItems().isNotEmpty()) {
                CartManager.clearCart()
                productAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Compra finalizada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
