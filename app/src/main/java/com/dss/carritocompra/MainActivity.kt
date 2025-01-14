package com.dss.carritocompra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.ProductAdapter
import com.dss.carritocompra.api.ApiClient
import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.ProductsResponse
import com.dss.carritocompra.utils.CartManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Productos"

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Botón para acceder al mapa
        val buttonOpenMap: FloatingActionButton = findViewById(R.id.buttonOpenMap)
        buttonOpenMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        // Botón para ver carrito
        val buttonOpenCart: FloatingActionButton = findViewById(R.id.buttonOpenCart)
        buttonOpenCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Llamar a la API para obtener los productos
        fetchProducts()
    }

    private fun fetchProducts() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.getAllProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    val products = productsResponse?._embedded?.products?.toMutableList() ?: mutableListOf()
                    Log.d("API_RESPONSE", "Products: $products")

                    // Crear el adapter para los productos
                    productAdapter = ProductAdapter(products) { product ->
                        CartManager.addProduct(product)
                        Toast.makeText(this@MainActivity, "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
                    }
                    recyclerView.adapter = productAdapter
                } else {
                    Log.e("API_ERROR", "Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }
}
