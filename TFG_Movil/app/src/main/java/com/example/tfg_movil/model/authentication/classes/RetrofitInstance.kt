package com.example.tfg_movil.model.authentication.classes

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Para emulador Android (10.0.2.2 = localhost del host)
    private const val BASE_URL = "https://10.0.2.2:7077"  // Asegúrate que coincide con tu puerto HTTPS del servidor C#

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authClient: AuthClient by lazy {
        retrofit.create(AuthClient::class.java)
    }
}