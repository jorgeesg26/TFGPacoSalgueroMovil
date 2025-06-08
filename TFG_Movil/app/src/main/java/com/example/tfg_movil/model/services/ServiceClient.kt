package com.example.tfg_movil.model.services

import retrofit2.http.GET

interface ServiceClient {
    @GET("/api/Service/get_services")
    suspend fun getAllServices(): List<Service>
}
