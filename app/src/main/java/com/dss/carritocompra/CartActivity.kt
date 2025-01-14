package com.dss.carritocompra

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

        // Obtener los productos del carrito
        val cartProducts = CartManager.getCartItems().toMutableList()

        // Configurar el adapter y manejar el clic en el botón "Eliminar"
        productAdapter = ProductAdapter(cartProducts) { product ->
            CartManager.removeProduct(product)  // Eliminar del carrito
            val position = cartProducts.indexOf(product)
            cartProducts.removeAt(position)  // Eliminar de la lista local
            productAdapter.notifyItemRemoved(position)  // Notificar el RecyclerView
            Toast.makeText(this, "${product.name} eliminado del carrito", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = productAdapter

        // Configurar el botón de "Checkout"
        val buttonCheckout: Button = findViewById(R.id.buttonCheckout)
        buttonCheckout.setOnClickListener {
            if (CartManager.getCartItems().isNotEmpty()) {
                CartManager.clearCart()  // Limpiar el carrito
                cartProducts.clear()  // Limpiar la lista local
                productAdapter.notifyDataSetChanged()  // Actualizar la vista
                Toast.makeText(this, "Compra finalizada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
