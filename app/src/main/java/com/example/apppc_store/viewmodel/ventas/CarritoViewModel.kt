package com.example.apppc_store.viewmodel.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppc_store.model.entidades.CarritoCompras
import com.example.apppc_store.model.entidades.EstadoVenta
import com.example.apppc_store.model.entidades.ItemCarrito
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.Venta
import com.example.apppc_store.model.repositorios.RepositorioProductos
import com.example.apppc_store.model.repositorios.RepositorioVentas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val repositorioVentas: RepositorioVentas,
    private val repositorioProductos: RepositorioProductos
) : ViewModel() {

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
                val itemExistente = carritoActual.items.find { it.productoId == producto.id }
                val stockRestante = (producto.stock - (itemExistente?.cantidad ?: 0)).coerceAtLeast(0)

                if (stockRestante <= 0) {
                    _error.value = "No hay stock disponible para ${producto.nombre}"
                    return@launch
                }

                val incrementoSeguro = cantidad.coerceAtMost(stockRestante)
                val nuevoCarrito = carritoActual.agregarItem(producto, incrementoSeguro)
                _carrito.value = nuevoCarrito

                _error.value = if (incrementoSeguro < cantidad) {
                    "Solo quedan $stockRestante unidades de ${producto.nombre}"
                } else {
                    null
                }
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

    fun actualizarCantidad(productoId: String, nuevaCantidad: Int, stockDisponible: Int? = null) {
        viewModelScope.launch {
            try {
                val carritoActual = _carrito.value
                val itemActual = carritoActual.items.find { it.productoId == productoId }
                    ?: return@launch
                val limite = stockDisponible ?: itemActual.stockDisponible
                val cantidadAjustada = nuevaCantidad.coerceIn(0, limite)
                _carrito.value = carritoActual.actualizarCantidad(productoId, cantidadAjustada)
                _error.value = if (cantidadAjustada < nuevaCantidad) {
                    "Alcanzaste el stock disponible para ${itemActual.nombreProducto}"
                } else {
                    null
                }
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

    fun obtenerCantidadTotal(): Int = _carrito.value.cantidadTotal

    fun obtenerMontoTotal(): Double = _carrito.value.montoTotal

    fun obtenerItemCarrito(productoId: String): ItemCarrito? {
        return _carrito.value.items.find { it.productoId == productoId }
    }

    fun limpiarError() {
        _error.value = null
    }
}
