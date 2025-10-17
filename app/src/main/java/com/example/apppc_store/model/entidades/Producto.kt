package com.example.apppc_store.model.entidades

data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val stock: Int,
    val imagenUrl: String? = null,
    val esActivo: Boolean = true,
    val tipoProducto: TipoProducto = TipoProducto.VENTA,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaActualizacion: Long = System.currentTimeMillis()
)

enum class TipoProducto {
    VENTA
}
