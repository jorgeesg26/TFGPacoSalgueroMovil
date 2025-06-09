package com.example.tfg_movil.model.services

import com.example.tfg_movil.model.authentication.classes.RetrofitInstance

class ServiceRepository {
    private val client = RetrofitInstance.serviceClient

    suspend fun fetchServices() = runCatching { client.getAllServices() }
    suspend fun createService(service: Service) = runCatching { client.createService(service) }
    suspend fun updateService(id: Int, service: Service) = runCatching { client.updateService(id, service) }
    suspend fun deleteService(id: Int) = runCatching { client.deleteService(id) }
}