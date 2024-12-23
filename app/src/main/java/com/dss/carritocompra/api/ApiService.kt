package com.dss.carritocompra.api

import com.dss.carritocompra.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/api/products")
    fun getAllProducts(): Call<List<Product>>

    @POST("/api/products/add")
    fun addProduct(@Query("name") name: String, @Query("price") price: Double): Call<Void>

    @POST("/api/products/edit/{id}")
    fun editProduct(@Path("id") id: Long, @Query("name") name: String, @Query("price") price: Double): Call<Void>

    @POST("/api/products/delete/{id}")
    fun deleteProduct(@Path("id") id: Long): Call<Void>
}
