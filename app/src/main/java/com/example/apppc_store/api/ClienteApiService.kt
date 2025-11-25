package com.example.apppc_store.api

import com.example.apppc_store.model.entidades.Cliente
import retrofit2.Response
import retrofit2.http.*

interface ClienteApiService {

    @GET("clientes")
    suspend fun obtenerTodosLosClientes(): Response<List<Cliente>>

    @GET("clientes/{id}")
    suspend fun obtenerClientePorId(@Path("id") id: String): Response<Cliente?>

    @POST("clientes")
    suspend fun crearCliente(@Body cliente: Cliente): Response<Unit>

    @PUT("clientes/{id}")
    suspend fun actualizarCliente(@Path("id") id: String, @Body cliente: Cliente): Response<Unit>

    @DELETE("clientes/{id}")
    suspend fun eliminarCliente(@Path("id") id: String): Response<Unit>
}
