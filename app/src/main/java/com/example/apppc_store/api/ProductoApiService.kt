package com.example.apppc_store.api

import com.example.apppc_store.model.entidades.Producto
import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {

    @GET("productos")
    suspend fun obtenerTodosLosProductos(): Response<List<Producto>>

    @GET("productos/{id}")
    suspend fun obtenerProductoPorId(@Path("id") id: String): Response<Producto?>

    @POST("productos")
    suspend fun crearProducto(@Body producto: Producto): Response<Unit>

    @PUT("productos/{id}")
    suspend fun actualizarProducto(@Path("id") id: String, @Body producto: Producto): Response<Unit>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: String): Response<Unit>
}
