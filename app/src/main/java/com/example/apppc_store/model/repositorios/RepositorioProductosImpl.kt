package com.example.apppc_store.model.repositorios

import com.example.apppc_store.api.ProductoApiService
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositorioProductosImpl @Inject constructor(
    private val productoApiService: ProductoApiService
) : RepositorioProductos {

    // Datos mock para pruebas (fallback si API falla)
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
        try {
            val response = productoApiService.obtenerTodosLosProductos()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                emit(productosMock)
            }
        } catch (e: Exception) {
            emit(productosMock)
        }
    }

    override suspend fun obtenerProductoPorId(id: String): Flow<Producto?> = flow {
        try {
            val response = productoApiService.obtenerProductoPorId(id)
            if (response.isSuccessful) {
                emit(response.body())
            } else {
                emit(productosMock.find { it.id == id })
            }
        } catch (e: Exception) {
            emit(productosMock.find { it.id == id })
        }
    }

    override suspend fun obtenerProductosPorCategoria(categoriaId: String): Flow<List<Producto>> = flow {
        try {
            val response = productoApiService.obtenerTodosLosProductos()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                emit(productos.filter { it.categoria == categoriaId })
            } else {
                emit(productosMock.filter { it.categoria == categoriaId })
            }
        } catch (e: Exception) {
            emit(productosMock.filter { it.categoria == categoriaId })
        }
    }

    override suspend fun buscarProductos(consulta: String): Flow<List<Producto>> = flow {
        try {
            val response = productoApiService.obtenerTodosLosProductos()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                emit(productos.filter {
                    it.nombre.contains(consulta, ignoreCase = true) ||
                    it.descripcion.contains(consulta, ignoreCase = true)
                })
            } else {
                emit(productosMock.filter {
                    it.nombre.contains(consulta, ignoreCase = true) ||
                    it.descripcion.contains(consulta, ignoreCase = true)
                })
            }
        } catch (e: Exception) {
            emit(productosMock.filter {
                it.nombre.contains(consulta, ignoreCase = true) ||
                it.descripcion.contains(consulta, ignoreCase = true)
            })
        }
    }

    override suspend fun obtenerProductosEnVenta(): Flow<List<Producto>> = flow {
        try {
            val response = productoApiService.obtenerTodosLosProductos()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                emit(productos.filter { it.tipoProducto == TipoProducto.VENTA })
            } else {
                emit(productosMock.filter { it.tipoProducto == TipoProducto.VENTA })
            }
        } catch (e: Exception) {
            emit(productosMock.filter { it.tipoProducto == TipoProducto.VENTA })
        }
    }

    override suspend fun obtenerProductosEnArriendo(): Flow<List<Producto>> = flow {
        try {
            val response = productoApiService.obtenerTodosLosProductos()
            if (response.isSuccessful) {
                val productos = response.body() ?: emptyList()
                emit(productos.filter { it.tipoProducto == TipoProducto.ARRIENDO })
            } else {
                emit(emptyList()) // No hay productos en arriendo en mock
            }
        } catch (e: Exception) {
            emit(emptyList()) // No hay productos en arriendo en mock
        }
    }

    override suspend fun insertarProducto(producto: Producto) {
        try {
            productoApiService.crearProducto(producto)
        } catch (e: Exception) {
            // Fallback to mock - no action needed
        }
    }

    override suspend fun actualizarProducto(producto: Producto) {
        try {
            productoApiService.actualizarProducto(producto.id, producto)
        } catch (e: Exception) {
            // Fallback to mock - no action needed
        }
    }

    override suspend fun eliminarProducto(id: String) {
        try {
            productoApiService.eliminarProducto(id)
        } catch (e: Exception) {
            // Fallback to mock - no action needed
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
            // Fallback to mock - no action needed
        }
    }
}
