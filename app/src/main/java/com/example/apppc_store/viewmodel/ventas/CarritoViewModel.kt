package com.example.apppc_store.viewmodel.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppc_store.model.entidades.CarritoCompras
import com.example.apppc_store.model.entidades.ItemCarrito
import com.example.apppc_store.model.entidades.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarritoViewModel : ViewModel() {

    private val _carrito = MutableStateFlow(CarritoCompras())
    val carrito: StateFlow<CarritoCompras> = _carrito.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1) {
        viewModelScope.launch {
            try {
                val carritoActual = _carrito.value
                _carrito.value = carritoActual.agregarItem(producto, cantidad)
            } catch (e: Exception) {
                _error.value = "Error al agregar producto al carrito: ${e.message}"
            }
        }
    }

    fun eliminarDelCarrito(productoId: String) {
        viewModelScope.launch {
            try {
                val carritoActual = _carrito.value
                _carrito.value = carritoActual.eliminarItem(productoId)
            } catch (e: Exception) {
                _error.value = "Error al eliminar producto del carrito: ${e.message}"
            }
        }
    }

    fun actualizarCantidad(productoId: String, nuevaCantidad: Int) {
        viewModelScope.launch {
            try {
                val carritoActual = _carrito.value
                _carrito.value = carritoActual.actualizarCantidad(productoId, nuevaCantidad)
            } catch (e: Exception) {
                _error.value = "Error al actualizar cantidad: ${e.message}"
            }
        }
    }

    fun limpiarCarrito() {
        viewModelScope.launch {
            try {
                _carrito.value = _carrito.value.limpiar()
            } catch (e: Exception) {
                _error.value = "Error al limpiar carrito: ${e.message}"
            }
        }
    }

    fun obtenerCantidadTotal(): Int {
        return _carrito.value.items.sumOf { it.cantidad }
    }

    fun obtenerMontoTotal(): Double {
        return _carrito.value.items.sumOf { it.precioTotal }
    }

    fun obtenerItemCarrito(productoId: String): ItemCarrito? {
        return _carrito.value.items.find { it.productoId == productoId }
    }

    fun limpiarError() {
        _error.value = null
    }
}
