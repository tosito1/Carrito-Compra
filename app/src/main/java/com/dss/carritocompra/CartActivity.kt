package com.dss.carritocompra

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.ProductAdapter
import com.dss.carritocompra.api.ApiClient
import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var apiService: ApiService

    // Lista local para almacenar los productos del carrito
    private val cartProducts: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Inicializar ApiService
        apiService = ApiClient.retrofit.create(ApiService::class.java)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCart)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador con una lista vacía
        productAdapter = ProductAdapter(cartProducts) { productId ->
            removeProductFromCart(productId) // Llama a la función para eliminar productos del carrito
        }
        recyclerView.adapter = productAdapter

        // Botón para finalizar la compra
        val buttonCheckout: Button = findViewById(R.id.buttonCheckout)
        buttonCheckout.setOnClickListener {
            if (cartProducts.isNotEmpty()) {
                checkoutAndClearCart()
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar los productos del carrito
        fetchCartProducts()
    }

    // Método para cargar los productos del carrito
    private fun fetchCartProducts() {
        apiService.getCartProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    // Obtener los productos del carrito directamente desde la respuesta JSON
                    val fetchedProducts = response.body() ?: listOf()

                    // Actualizar la lista local de productos y notificar al adaptador
                    cartProducts.clear()
                    cartProducts.addAll(fetchedProducts)
                    productAdapter.notifyDataSetChanged()

                    // Mostrar un mensaje de éxito
                    Toast.makeText(this@CartActivity, "Productos cargados", Toast.LENGTH_SHORT).show()
                } else {
                    // Manejar error en la respuesta
                    Toast.makeText(this@CartActivity, "Error al cargar el carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                // Manejar error de red
                Toast.makeText(this@CartActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para eliminar un producto del carrito
    private fun removeProductFromCart(productId: Long) {
        apiService.removeProductFromCart(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Eliminar el producto de la lista local y notificar al adaptador
                    cartProducts.removeIf { it.id == productId }
                    productAdapter.notifyDataSetChanged()

                    Toast.makeText(this@CartActivity, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@CartActivity, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CartActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Método para finalizar la compra
    private fun checkoutAndClearCart() {
        // Llamar al endpoint de checkout primero
        apiService.checkoutCart().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // La respuesta del checkout fue exitosa
                    // Ahora, limpiar el carrito llamando a /api/cart/clear
                    clearCart()
                } else {
                    // Si hubo un error en el checkout
                    Toast.makeText(this@CartActivity, "Error al finalizar la compra", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Error de red
                Toast.makeText(this@CartActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearCart() {
        // Llamar al endpoint de /api/cart/clear para limpiar el carrito
        apiService.clearCart().enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Si la respuesta es exitosa, vaciar el carrito local y actualizar la vista
                    cartProducts.clear()
                    productAdapter.notifyDataSetChanged()

                    // Mostrar mensaje de éxito
                    Toast.makeText(this@CartActivity, "Compra finalizada y carrito vaciado", Toast.LENGTH_SHORT).show()

                    // Volver a MainActivity después de finalizar la compra
                    val intent = Intent(this@CartActivity, MainActivity::class.java)
                    startActivity(intent)

                    // Cerrar CartActivity para que no quede en el stack
                    finish()
                } else {
                    // Si hay un error en limpiar el carrito
                    Toast.makeText(this@CartActivity, "Error al limpiar el carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Error de red
                Toast.makeText(this@CartActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



}
