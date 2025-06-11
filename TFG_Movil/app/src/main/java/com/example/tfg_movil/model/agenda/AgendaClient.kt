package com.example.tfg_movil.model.agenda

import retrofit2.http.*
import retrofit2.Response

interface AgendaClient {
    @GET("api/Agenda")
    suspend fun getEntradas(): List<EntradaAgenda>

    @POST("api/Agenda")
    suspend fun create(@Body entrada: EntradaAgenda): Response<EntradaAgenda>

    @GET("api/Agenda/mes/{year}/{month}")
    suspend fun getEntradasPorMes(@Path("year") year: Int, @Path("month") month: Int): List<EntradaAgenda>
}