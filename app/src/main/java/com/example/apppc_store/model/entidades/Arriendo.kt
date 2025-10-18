package com.example.apppc_store.model.entidades

data class Arriendo(
    val id: String,
    val clienteId: String,
    val productoId: String,
    val nombreProducto: String,
    val fechaInicio: Long,
    val fechaFin: Long,
    val precioPorDia: Double,
    val totalDias: Int,
    val montoTotal: Double,
    val estado: EstadoArriendo,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val notas: String? = null
)

enum class EstadoArriendo {
    SOLICITADO,
    CONFIRMADO,
    EN_CURSO,
    FINALIZADO,
    CANCELADO
}
