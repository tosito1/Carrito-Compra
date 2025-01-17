package com.dss.carritocompra

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.CartAdapter
import com.dss.carritocompra.api.ApiClient
import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var apiService: ApiService
    private lateinit var emptyCartMessage: TextView

    private val cartProducts: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializar ApiService
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Mensaje para carrito vacío
        emptyCartMessage = findViewById(R.id.emptyCartMessage)

        // Inicializar el adaptador con la lista local
        cartAdapter = CartAdapter(cartProducts) { product ->
            removeProductFromCart(product.id)
        }
        recyclerView.adapter = cartAdapter

        // Botón para finalizar compra
        val buttonCheckout: Button = findViewById(R.id.buttonCheckout)
        buttonCheckout.setOnClickListener {
            if (cartProducts.isNotEmpty()) {
                // Abrir la actividad de confirmación de compra
                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtra("cartProducts", ArrayList(cartProducts)) // Pasar productos al checkout
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar los productos del carrito
        fetchCartProducts()
    }

    private fun fetchCartProducts() {
        apiService.getCartProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val fetchedProducts = response.body() ?: listOf()
                    cartProducts.clear()
                    cartProducts.addAll(fetchedProducts)
                    cartAdapter.notifyDataSetChanged()
                    updateCartVisibility()
                } else {
                    Toast.makeText(this@CartActivity, "Error al cargar el carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun removeProductFromCart(productId: Long) {
        apiService.removeProductFromCart(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val productToRemove = cartProducts.find { it.id == productId }
                    productToRemove?.let {
                        cartProducts.remove(it)
                        cartAdapter.removeProduct(it)
                        updateCartVisibility()
                    }
                } else {
                    Toast.makeText(this@CartActivity, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateCartVisibility() {
        if (cartProducts.isEmpty()) {
            emptyCartMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyCartMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}