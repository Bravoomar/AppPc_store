package com.example.apppc_store.viewmodel.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import com.example.apppc_store.model.repositorios.RepositorioProductos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductosViewModel @Inject constructor(
    private val repositorioProductos: RepositorioProductos
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

    private val _productosEnVenta = MutableStateFlow<List<Producto>>(emptyList())
    val productosEnVenta: StateFlow<List<Producto>> = _productosEnVenta.asStateFlow()

    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioProductos.obtenerTodosLosProductos().collect { listaProductos ->
                    _productos.value = listaProductos
                    _productosEnVenta.value = listaProductos.filter { it.tipoProducto == TipoProducto.VENTA }
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun cargarProductosPorCategoria(categoriaId: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioProductos.obtenerProductosPorCategoria(categoriaId).collect { listaProductos ->
                    _productos.value = listaProductos
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar productos por categoría: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun buscarProductos(consulta: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioProductos.buscarProductos(consulta).collect { listaProductos ->
                    _productos.value = listaProductos
                }
            } catch (e: Exception) {
                _error.value = "Error al buscar productos: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun seleccionarProducto(producto: Producto) {
        _productoSeleccionado.value = producto
    }

    fun agregarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repositorioProductos.insertarProducto(producto)
                cargarProductos() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al agregar producto: ${e.message}"
            }
        }
    }

    fun actualizarProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repositorioProductos.actualizarProducto(producto)
                cargarProductos() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al actualizar producto: ${e.message}"
            }
        }
    }

    fun eliminarProducto(productoId: String) {
        viewModelScope.launch {
            try {
                repositorioProductos.eliminarProducto(productoId)
                cargarProductos() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al eliminar producto: ${e.message}"
            }
        }
    }

    fun actualizarStock(productoId: String, nuevoStock: Int) {
        viewModelScope.launch {
            try {
                repositorioProductos.actualizarStock(productoId, nuevoStock)
                cargarProductos() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al actualizar stock: ${e.message}"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
