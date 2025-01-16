package com.dss.carritocompra.api

import com.dss.carritocompra.models.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import com.dss.carritocompra.models.LoginResponse
import com.dss.carritocompra.models.ProductsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface ApiService {

    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<Void>

    // Métodos relacionados a los productos
    @GET("/api/products")
    fun getAllProducts(): Call<ProductsResponse>

    @POST("/api/products/add")
    fun addProduct(@Query("name") name: String, @Query("price") price: Double): Call<Void>

    @POST("/api/products/edit/{id}")
    fun editProduct(@Path("id") id: Long, @Query("name") name: String, @Query("price") price: Double): Call<Void>

    @POST("/api/products/delete/{id}")
    fun deleteProduct(@Path("id") id: Long): Call<Void>

    // Métodos relacionados al carrito
    @POST("/cart/add/{id}")
    fun addProductToCart(@Path("id") productId: Long): Call<Void>

}
