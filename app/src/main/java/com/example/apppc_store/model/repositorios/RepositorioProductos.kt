package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Producto
import kotlinx.coroutines.flow.Flow

interface RepositorioProductos {
    suspend fun obtenerTodosLosProductos(): Flow<List<Producto>>
    suspend fun obtenerProductoPorId(id: String): Flow<Producto?>
    suspend fun obtenerProductosPorCategoria(categoriaId: String): Flow<List<Producto>>
    suspend fun buscarProductos(consulta: String): Flow<List<Producto>>
    suspend fun obtenerProductosEnVenta(): Flow<List<Producto>>
    suspend fun obtenerProductosEnArriendo(): Flow<List<Producto>>
    suspend fun insertarProducto(producto: Producto)
    suspend fun actualizarProducto(producto: Producto)
    suspend fun eliminarProducto(id: String)
    suspend fun actualizarStock(productoId: String, nuevoStock: Int)
}
