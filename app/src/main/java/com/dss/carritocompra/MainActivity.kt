package com.dss.carritocompra

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.ProductAdapter
import com.dss.carritocompra.api.ApiClient
import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.Product
import com.dss.carritocompra.models.ProductsResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var apiService: ApiService
    private val products: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar Toolbar y DrawerLayout
        val toolbar: Toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar NavigationView
        val navView: NavigationView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter(products) { productId ->
            addProductToCart(productId) // Llama a la función para añadir el producto al carrito
        }
        recyclerView.adapter = productAdapter

        // Botón para añadir producto
        val fabAddProduct: FloatingActionButton = findViewById(R.id.fab)
        fabAddProduct.setOnClickListener {
            Toast.makeText(this, "Añadir Producto", Toast.LENGTH_SHORT).show()
            // Lógica para añadir un producto
        }

        // Configuración de los FABs
        val buttonOpenMap: FloatingActionButton = findViewById(R.id.buttonOpenMap)
        val buttonOpenCart: FloatingActionButton = findViewById(R.id.buttonOpenCart)
        // Acciones para los botones FAB
        buttonOpenMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java) // Asegúrate de tener la actividad MapActivity
            startActivity(intent)
        }

        buttonOpenCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java) // Asegúrate de tener la actividad CartActivity
            startActivity(intent)
        }

        apiService = ApiClient.retrofit.create(ApiService::class.java)
        fetchProducts()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_login -> {
                // Navegar a la pantalla de Login
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            // Otras opciones del Navigation Drawer se pueden manejar aquí
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun fetchProducts() {
        apiService.getAllProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    val fetchedProducts = productsResponse?._embedded?.products ?: listOf()

                    for (product in fetchedProducts) {
                        val productLink = product._links?.self?.href
                        val productId = productLink?.substringAfterLast("/")?.toLong() ?: 0L
                        product.id = productId // Asignar el ID al producto
                    }

                    products.clear()
                    products.addAll(fetchedProducts)
                    productAdapter.notifyDataSetChanged()

                    Toast.makeText(this@MainActivity, "Productos cargados", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductsResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
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
