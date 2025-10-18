package com.example.apppc_store.model.entidades

data class Cliente(
    val id: String,
    val nombre: String,
    val email: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val esActivo: Boolean = true,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaActualizacion: Long = System.currentTimeMillis()
)
