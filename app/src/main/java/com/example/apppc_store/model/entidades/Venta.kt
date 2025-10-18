package com.example.apppc_store.model.entidades

data class Venta(
    val id: String,
    val clienteId: String,
    val items: List<ItemVenta>,
    val montoTotal: Double,
    val estado: EstadoVenta,
    val fechaVenta: Long = System.currentTimeMillis(),
    val notas: String? = null
)

data class ItemVenta(
    val id: Long = 0,
    val ventaId: String,
    val productoId: String,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val precioTotal: Double
)

enum class EstadoVenta {
    PENDIENTE,
    CONFIRMADA,
    PROCESANDO,
    ENVIADA,
    ENTREGADA,
    CANCELADA
}
