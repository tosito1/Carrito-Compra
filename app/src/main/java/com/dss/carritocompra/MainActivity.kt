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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import com.dss.carritocompra.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var apiService: ApiService
    private val products: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = ApiClient.retrofit.create(ApiService::class.java)

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

        val fabAddProduct: FloatingActionButton = findViewById(R.id.fab)
        fabAddProduct.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        productAdapter = ProductAdapter(products) { productId ->
            addProductToCart(productId) // Llama a la función para realizar la solicitud GET
        }
        recyclerView.adapter = productAdapter

        // Llamar a la API para obtener los productos
        fetchProducts()

    }

    private fun fetchProducts() {
        apiService.getAllProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(
                call: Call<ProductsResponse>,
                response: Response<ProductsResponse>
            ) {
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    val fetchedProducts = productsResponse?._embedded?.products ?: listOf()

                    // Ahora asignamos el id directamente al Product
                    for (product in fetchedProducts) {
                        val productLink = product._links?.self?.href
                        val productId = productLink?.substringAfterLast("/")?.toLong() ?: 0L

                        // Asignamos el id al producto
                        product.id = productId // Modificar el campo id directamente en el Product

                        // Verificación en log
                        Log.d("FETCH_PRODUCTS", "Producto recibido: ID=${product.id}, Nombre=${product.name}, Precio=${product.price}")
                    }

                    // Actualiza la lista local y notifica al adaptador
                    products.clear()
                    products.addAll(fetchedProducts)
                    productAdapter.notifyDataSetChanged()

                    Toast.makeText(this@MainActivity, "Productos cargados", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("API_ERROR", "Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
            }
        })
    }



    private fun addProductToCart(productId: Long) {
        apiService.addProductToCart(productId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Producto añadido al carrito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error al añadir al carrito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
