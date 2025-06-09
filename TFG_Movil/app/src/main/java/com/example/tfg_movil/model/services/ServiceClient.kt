package com.example.tfg_movil.model.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceClient {
    @GET("/api/Service/get_services")
    suspend fun getAllServices(): List<Service>
    @POST("/api/Service/create")
    suspend fun createService(@Body service: Service): Response<Unit>

    @PUT("/api/Service/update/{id}")
    suspend fun updateService(@Path("id") id: Int, @Body service: Service): Response<Unit>

    @DELETE("/api/Service/delete/{id}")
    suspend fun deleteService(@Path("id") id: Int): Response<Unit>
}
