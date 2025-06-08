package com.example.tfg_movil.model.services

import com.example.tfg_movil.model.authentication.classes.RetrofitInstance

class ServiceRepository {
    private val client = RetrofitInstance.serviceClient

    suspend fun fetchServices(): Result<List<Service>> {
        return try {
            val response = client.getAllServices()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
