package com.example.apppc_store.model.repositorios

import com.example.apppc_store.api.ProductoApiService
import com.example.apppc_store.data.local.dao.ProductoDao
import com.example.apppc_store.data.local.entities.ProductoEntity
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositorioProductosImpl @Inject constructor(
    private val productoApiService: ProductoApiService,
    private val productoDao: ProductoDao
) : RepositorioProductos {

    // Datos mock para pruebas (fallback si API falla)
    private val productosMock = listOf(
        Producto(
            id = "1",
            nombre = "Monitor 160Hz",
            descripcion = "Monitor gaming de 27 pulgadas con frecuencia de actualización de 160Hz, ideal para gaming competitivo",
            precio = 450.0,
            categoria = "Monitores",
            stock = 8,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "2",
            nombre = "Kit Gamer",
            descripcion = "Kit completo gamer: teclado mecánico RGB, mouse gaming, pad y auriculares",
            precio = 320.0,
            categoria = "Periféricos",
            stock = 12,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "3",
            nombre = "Tarjeta Gráfica RTX 4060",
            descripcion = "Tarjeta gráfica NVIDIA RTX 4060 con 8GB GDDR6, perfecta para gaming y streaming",
            precio = 550.0,
            categoria = "Componentes",
            stock = 5,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "4",
            nombre = "Procesador AMD Ryzen 7 5800X",
            descripcion = "Procesador AMD Ryzen 7 5800X de 8 núcleos y 16 hilos, excelente rendimiento para gaming y productividad",
            precio = 380.0,
            categoria = "Componentes",
            stock = 6,
            tipoProducto = TipoProducto.VENTA
        )
    )

    override suspend fun obtenerTodosLosProductos(): Flow<List<Producto>> = flow {
        try {
            // Primero intentar obtener de Room
            val productosLocal = productoDao.obtenerTodosLosProductos().first()
            if (productosLocal.isNotEmpty()) {
                emit(productosLocal.map { it.toProducto() })
                return@flow
            }
            
            // Si Room está vacío, intentar obtener de la API
            try {
                val response = productoApiService.obtenerTodosLosProductos()
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val productosApi = response.body()!!
                    // Guardar en Room
                    productosApi.forEach { producto ->
                        productoDao.insertarProducto(ProductoEntity.fromProducto(producto))
                    }
                    emit(productosApi)
                } else {
                    // Si la API falla, usar mock y guardarlos en Room
                    productosMock.forEach { producto ->
                        productoDao.insertarProducto(ProductoEntity.fromProducto(producto))
                    }
                    emit(productosMock)
                }
            } catch (apiException: Exception) {
                // Si la API falla, usar mock y guardarlos en Room
                productosMock.forEach { producto ->
                    productoDao.insertarProducto(ProductoEntity.fromProducto(producto))
                }
                emit(productosMock)
            }
        } catch (e: Exception) {
            // Si hay error general, usar mock
            emit(productosMock)
        }
    }

    override suspend fun obtenerProductoPorId(id: String): Flow<Producto?> = flow {
        try {
            val response = productoApiService.obtenerProductoPorId(id)
            if (response.isSuccessful && response.body() != null) {
                val producto = response.body()!!
                productoDao.insertarProducto(ProductoEntity.fromProducto(producto))
                emit(producto)
            } else {
                val productoLocal = productoDao.obtenerProductoPorId(id).first()
                emit(productoLocal?.toProducto() ?: productosMock.find { it.id == id })
            }
        } catch (e: Exception) {
            val productoLocal = productoDao.obtenerProductoPorId(id).first()
            emit(productoLocal?.toProducto() ?: productosMock.find { it.id == id })
        }
    }

    override suspend fun obtenerProductosPorCategoria(categoriaId: String): Flow<List<Producto>> {
        return productoDao.obtenerProductosPorCategoria(categoriaId).map { entities ->
            entities.map { it.toProducto() }
        }
    }

    override suspend fun buscarProductos(consulta: String): Flow<List<Producto>> {
        return productoDao.buscarProductos(consulta).map { entities ->
            entities.map { it.toProducto() }
        }
    }

    override suspend fun obtenerProductosEnVenta(): Flow<List<Producto>> {
        return productoDao.obtenerProductosEnVenta().map { entities ->
            entities.map { it.toProducto() }
        }
    }

    override suspend fun obtenerProductosEnArriendo(): Flow<List<Producto>> {
        return productoDao.obtenerProductosEnArriendo().map { entities ->
            entities.map { it.toProducto() }
        }
    }

    override suspend fun insertarProducto(producto: Producto) {
        try {
            productoApiService.crearProducto(producto)
        } catch (e: Exception) {
            // Si falla la API, guardar solo en local
        } finally {
            // Siempre guardar en Room
            productoDao.insertarProducto(ProductoEntity.fromProducto(producto))
        }
    }

    override suspend fun actualizarProducto(producto: Producto) {
        try {
            productoApiService.actualizarProducto(producto.id, producto)
        } catch (e: Exception) {
            // Si falla la API, actualizar solo en local
        } finally {
            // Siempre actualizar en Room
            productoDao.actualizarProducto(ProductoEntity.fromProducto(producto))
        }
    }

    override suspend fun eliminarProducto(id: String) {
        try {
            productoApiService.eliminarProducto(id)
        } catch (e: Exception) {
            // Si falla la API, eliminar solo en local
        } finally {
            // Siempre eliminar en Room (soft delete)
            productoDao.eliminarProducto(id)
        }
    }

    override suspend fun actualizarStock(productoId: String, nuevoStock: Int) {
        try {
            val response = productoApiService.obtenerProductoPorId(productoId)
            if (response.isSuccessful && response.body() != null) {
                val productoActualizado = response.body()!!.copy(stock = nuevoStock)
                productoApiService.actualizarProducto(productoId, productoActualizado)
            }
        } catch (e: Exception) {
            // Si falla la API, actualizar solo en local
        } finally {
            // Siempre actualizar stock en Room
            productoDao.actualizarStock(productoId, nuevoStock)
        }
    }
}
