package com.example.apppc_store.viewmodel.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppc_store.model.entidades.Cliente
import com.example.apppc_store.model.repositorios.RepositorioClientes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientesViewModel @Inject constructor(
    private val repositorioClientes: RepositorioClientes
) : ViewModel() {

    private val _clientes = MutableStateFlow<List<Cliente>>(emptyList())
    val clientes: StateFlow<List<Cliente>> = _clientes.asStateFlow()

    private val _clienteSeleccionado = MutableStateFlow<Cliente?>(null)
    val clienteSeleccionado: StateFlow<Cliente?> = _clienteSeleccionado.asStateFlow()

    private val _estaCargando = MutableStateFlow(false)
    val estaCargando: StateFlow<Boolean> = _estaCargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargarClientes()
    }

    fun cargarClientes() {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioClientes.obtenerTodosLosClientes().collect { listaClientes ->
                    _clientes.value = listaClientes
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar clientes: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun buscarClientes(consulta: String) {
        viewModelScope.launch {
            _estaCargando.value = true
            try {
                repositorioClientes.buscarClientes(consulta).collect { listaClientes ->
                    _clientes.value = listaClientes
                }
            } catch (e: Exception) {
                _error.value = "Error al buscar clientes: ${e.message}"
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun seleccionarCliente(cliente: Cliente) {
        _clienteSeleccionado.value = cliente
    }

    fun agregarCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repositorioClientes.insertarCliente(cliente)
                cargarClientes() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al agregar cliente: ${e.message}"
            }
        }
    }

    fun actualizarCliente(cliente: Cliente) {
        viewModelScope.launch {
            try {
                repositorioClientes.actualizarCliente(cliente)
                cargarClientes() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al actualizar cliente: ${e.message}"
            }
        }
    }

    fun eliminarCliente(clienteId: String) {
        viewModelScope.launch {
            try {
                repositorioClientes.eliminarCliente(clienteId)
                cargarClientes() // Recargar la lista
            } catch (e: Exception) {
                _error.value = "Error al eliminar cliente: ${e.message}"
            }
        }
    }

    fun obtenerClientePorEmail(email: String) {
        viewModelScope.launch {
            try {
                repositorioClientes.obtenerClientePorEmail(email).collect { cliente ->
                    _clienteSeleccionado.value = cliente
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener cliente por email: ${e.message}"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
