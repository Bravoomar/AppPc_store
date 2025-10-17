package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositorioProductosImpl : RepositorioProductos {
    
    // Datos mock para pruebas
    private val productosMock = listOf(
        Producto(
            id = "1",
            nombre = "Laptop Gaming",
            descripcion = "Laptop para gaming de alta gama",
            precio = 1500.0,
            categoria = "Tecnología",
            stock = 5,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "2",
            nombre = "Monitor Gaming 4K",
            descripcion = "Monitor ultra HD de 32 pulgadas para gaming profesional",
            precio = 800.0,
            categoria = "Tecnología",
            stock = 1,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "3",
            nombre = "Smartphone",
            descripcion = "Teléfono inteligente de última generación",
            precio = 600.0,
            categoria = "Tecnología",
            stock = 10,
            tipoProducto = TipoProducto.VENTA
        )
    )

    override suspend fun obtenerTodosLosProductos(): Flow<List<Producto>> = flow {
        emit(productosMock)
    }

    override suspend fun obtenerProductoPorId(id: String): Flow<Producto?> = flow {
        emit(productosMock.find { it.id == id })
    }

    override suspend fun obtenerProductosPorCategoria(categoriaId: String): Flow<List<Producto>> = flow {
        emit(productosMock.filter { it.categoria == categoriaId })
    }

    override suspend fun buscarProductos(consulta: String): Flow<List<Producto>> = flow {
        emit(productosMock.filter { 
            it.nombre.contains(consulta, ignoreCase = true) ||
            it.descripcion.contains(consulta, ignoreCase = true)
        })
    }

    override suspend fun obtenerProductosEnVenta(): Flow<List<Producto>> = flow {
        emit(productosMock.filter { it.tipoProducto == TipoProducto.VENTA })
    }

    override suspend fun obtenerProductosEnArriendo(): Flow<List<Producto>> = flow {
        emit(emptyList()) // No hay productos en arriendo
    }

    override suspend fun insertarProducto(producto: Producto) {
        // TODO: Implementar cuando se configure Room
    }

    override suspend fun actualizarProducto(producto: Producto) {
        // TODO: Implementar cuando se configure Room
    }

    override suspend fun eliminarProducto(id: String) {
        // TODO: Implementar cuando se configure Room
    }

    override suspend fun actualizarStock(productoId: String, nuevoStock: Int) {
        // TODO: Implementar cuando se configure Room
    }
}
