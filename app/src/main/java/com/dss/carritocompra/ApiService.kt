package com.dss.carritocompra

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/products")
    fun getAllProducts(): Call<List<Product>>
}
