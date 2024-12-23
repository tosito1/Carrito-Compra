package com.dss.carritocompra

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dss.carritocompra.adapters.ProductAdapter
import com.dss.carritocompra.api.ApiClient
import com.dss.carritocompra.api.ApiService
import com.dss.carritocompra.models.Product
import com.dss.carritocompra.models.ProductsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Llamar a la API para obtener los productos
        fetchProducts()
    }


    private fun fetchProducts() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)

        apiService.getAllProducts().enqueue(object : Callback<ProductsResponse> {
            override fun onResponse(call: Call<ProductsResponse>, response: Response<ProductsResponse>) {
                if (response.isSuccessful) {
                    val productsResponse = response.body()
                    val products = productsResponse?._embedded?.products ?: emptyList()
                    Log.d("API_RESPONSE", "Products: $products")

                    // Actualiza el adaptador con los datos obtenidos
                    productAdapter = ProductAdapter(products)
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
