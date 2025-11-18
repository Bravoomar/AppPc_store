package com.example.apppc_store.model.entidades

data class CarritoCompras(
    val items: List<ItemCarrito> = emptyList()
) {
    val montoTotal: Double
        get() = items.sumOf { it.precioTotal }

    val cantidadTotal: Int
        get() = items.sumOf { it.cantidad }

    fun agregarItem(producto: Producto, cantidad: Int): CarritoCompras {
        if (cantidad <= 0 || producto.stock <= 0) return this

        val itemExistente = items.find { it.productoId == producto.id }
        val stockDisponible = producto.stock.coerceAtLeast(0)
        val incrementoPermitido = if (itemExistente != null) {
            val restante = (stockDisponible - itemExistente.cantidad).coerceAtLeast(0)
            cantidad.coerceAtMost(restante)
        } else {
            cantidad.coerceAtMost(stockDisponible)
        }

        if (incrementoPermitido <= 0) return this

        val nuevosItems = if (itemExistente != null) {
            items.map { item ->
                if (item.productoId == producto.id) {
                    item.copy(
                        cantidad = item.cantidad + incrementoPermitido,
                        stockDisponible = stockDisponible
                    )
                } else {
                    item
                }
            }
        } else {
            items + ItemCarrito(
                productoId = producto.id,
                nombreProducto = producto.nombre,
                precio = producto.precio,
                cantidad = incrementoPermitido,
                imagenUrl = producto.imagenUrl,
                stockDisponible = stockDisponible
            )
        }

        return copy(items = nuevosItems)
    }

    fun eliminarItem(productoId: String): CarritoCompras {
        val nuevosItems = items.filter { it.productoId != productoId }
        return copy(items = nuevosItems)
    }

    fun actualizarCantidad(productoId: String, nuevaCantidad: Int): CarritoCompras {
        val itemObjetivo = items.find { it.productoId == productoId } ?: return this
        val cantidadAjustada = nuevaCantidad.coerceIn(0, itemObjetivo.stockDisponible)
        if (cantidadAjustada <= 0) {
            return eliminarItem(productoId)
        }
        val nuevosItems = items.map { item ->
            if (item.productoId == productoId) {
                item.copy(cantidad = cantidadAjustada)
            } else {
                item
            }
        }
        return copy(items = nuevosItems)
    }

    fun limpiar(): CarritoCompras = CarritoCompras()
}

data class ItemCarrito(
    val productoId: String,
    val nombreProducto: String,
    val precio: Double,
    val cantidad: Int,
    val imagenUrl: String? = null,
    val stockDisponible: Int
) {
    val precioTotal: Double
        get() = precio * cantidad

    val stockRestante: Int
        get() = (stockDisponible - cantidad).coerceAtLeast(0)

    val avanceSobreStock: Float
        get() = if (stockDisponible == 0) {
            1f
        } else {
            cantidad.toFloat() / stockDisponible.toFloat()
        }
}
