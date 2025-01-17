package com.dss.carritocompra.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"
//   private const val BASE_URL = "http://192.168.18.222:8080/"

    private val client = OkHttpClient.Builder()
        .cookieJar(CookieManager()) // Usar el CookieManager personalizado
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client) // Añadir el cliente con soporte para cookies
        .build()
}

