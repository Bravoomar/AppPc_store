package com.example.apppc_store.model.entidades

data class Categoria(
    val id: String,
    val nombre: String,
    val descripcion: String? = null,
    val imagenUrl: String? = null,
    val esActiva: Boolean = true,
    val fechaCreacion: Long = System.currentTimeMillis()
)
