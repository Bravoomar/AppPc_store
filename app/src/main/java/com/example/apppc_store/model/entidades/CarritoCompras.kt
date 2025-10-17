package com.example.apppc_store.model.entidades

data class CarritoCompras(
    val items: List<ItemCarrito> = emptyList(),
    val montoTotal: Double = 0.0
) {
    fun agregarItem(producto: Producto, cantidad: Int): CarritoCompras {
        val itemExistente = items.find { it.productoId == producto.id }
        val nuevosItems = if (itemExistente != null) {
            items.map { if (it.productoId == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it }
        } else {
            items + ItemCarrito(
                productoId = producto.id,
                nombreProducto = producto.nombre,
                precio = producto.precio,
                cantidad = cantidad,
                imagenUrl = producto.imagenUrl
            )
        }
        return CarritoCompras(items = nuevosItems)
    }

    fun eliminarItem(productoId: String): CarritoCompras {
        val nuevosItems = items.filter { it.productoId != productoId }
        return CarritoCompras(items = nuevosItems)
    }

    fun actualizarCantidad(productoId: String, nuevaCantidad: Int): CarritoCompras {
        if (nuevaCantidad <= 0) {
            return eliminarItem(productoId)
        }
        val nuevosItems = items.map {
            if (it.productoId == productoId) it.copy(cantidad = nuevaCantidad) else it
        }
        return CarritoCompras(items = nuevosItems)
    }

    fun limpiar(): CarritoCompras {
        return CarritoCompras()
    }
}

data class ItemCarrito(
    val productoId: String,
    val nombreProducto: String,
    val precio: Double,
    val cantidad: Int,
    val imagenUrl: String? = null
) {
    val precioTotal: Double
        get() = precio * cantidad
}
