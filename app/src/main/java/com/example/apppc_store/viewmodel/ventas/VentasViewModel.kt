package com.example.apppc_store.viewmodel.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppc_store.model.entidades.EstadoVenta
import com.example.apppc_store.model.entidades.Venta
import com.example.apppc_store.model.repositorios.RepositorioVentas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VentasViewModel(
    private val repositorioVentas: RepositorioVentas
) : ViewModel() {

    private val _ventas = MutableStateFlow<List<Venta>>(emptyList())
    val ventas: StateFlow<List<Venta>> = _ventas.asStateFlow()

    private val _ventaSeleccionada = MutableStateFlow<Venta?>(null)
    val ventaSeleccionada: StateFlow<Venta?> = _ventaSeleccionada.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarVentas()
    }

    fun cargarVentas() {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioVentas.obtenerTodasLasVentas().collect { listaVentas ->
                    _ventas.value = listaVentas
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar ventas: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun cargarVentasPorCliente(clienteId: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioVentas.obtenerVentasPorCliente(clienteId).collect { listaVentas ->
                    _ventas.value = listaVentas
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar ventas del cliente: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun cargarVentasPorEstado(estado: EstadoVenta) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioVentas.obtenerVentasPorEstado(estado.name).collect { listaVentas ->
                    _ventas.value = listaVentas
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar ventas por estado: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun seleccionarVenta(venta: Venta) {
        _ventaSeleccionada.value = venta
    }

    fun crearVenta(venta: Venta) {
        viewModelScope.launch {
            try {
                repositorioVentas.insertarVenta(venta)
                cargarVentas() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al crear venta: ${e.message}"
            }
        }
    }

    fun actualizarVenta(venta: Venta) {
        viewModelScope.launch {
            try {
                repositorioVentas.actualizarVenta(venta)
                cargarVentas() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al actualizar venta: ${e.message}"
            }
        }
    }

    fun actualizarEstadoVenta(ventaId: String, nuevoEstado: EstadoVenta) {
        viewModelScope.launch {
            try {
                repositorioVentas.actualizarEstadoVenta(ventaId, nuevoEstado.name)
                cargarVentas() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al actualizar estado de venta: ${e.message}"
            }
        }
    }

    fun eliminarVenta(ventaId: String) {
        viewModelScope.launch {
            try {
                repositorioVentas.eliminarVenta(ventaId)
                cargarVentas() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al eliminar venta: ${e.message}"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
