package com.example.apppc_store.model.repositorios

import com.example.apppc_store.api.ClienteApiService
import com.example.apppc_store.model.entidades.Cliente
import com.example.apppc_store.model.entidades.RolCliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositorioClientesImpl @Inject constructor(
    private val clienteApiService: ClienteApiService
) : RepositorioClientes {

    // Datos mock para pruebas (fallback si API falla)
    private val codigoAccesoAdministrador = "APPC-ADMIN-01"
    private val administradorPorDefecto = Cliente(
        id = "admin",
        nombre = "Administrador General",
        email = "admin@appcstore.com",
        telefono = "999999999",
        direccion = "Oficina Central",
        rol = RolCliente.ADMIN
    )

    private val clientesMock = mutableListOf(
        administradorPorDefecto,
        Cliente(
            id = "1",
            nombre = "Juan Pérez",
            email = "juan.perez@email.com",
            telefono = "123456789",
            direccion = "Calle Principal 123"
        ),
        Cliente(
            id = "2",
            nombre = "María García",
            email = "maria.garcia@email.com",
            telefono = "987654321",
            direccion = "Avenida Central 456"
        )
    )

    override suspend fun obtenerTodosLosClientes(): Flow<List<Cliente>> = flow {
        try {
            val response = clienteApiService.obtenerTodosLosClientes()
            if (response.isSuccessful) {
                emit(response.body() ?: emptyList())
            } else {
                emit(clientesMock.toList())
            }
        } catch (e: Exception) {
            emit(clientesMock.toList())
        }
    }

    override suspend fun obtenerClientePorId(id: String): Flow<Cliente?> = flow {
        try {
            val response = clienteApiService.obtenerClientePorId(id)
            if (response.isSuccessful) {
                emit(response.body())
            } else {
                emit(clientesMock.find { it.id == id })
            }
        } catch (e: Exception) {
            emit(clientesMock.find { it.id == id })
        }
    }

    override suspend fun obtenerClientePorEmail(email: String): Flow<Cliente?> = flow {
        try {
            val response = clienteApiService.obtenerTodosLosClientes()
            if (response.isSuccessful) {
                val clientes = response.body() ?: emptyList()
                emit(clientes.find { it.email.equals(email, ignoreCase = true) })
            } else {
                emit(clientesMock.find { it.email.equals(email, ignoreCase = true) })
            }
        } catch (e: Exception) {
            emit(clientesMock.find { it.email.equals(email, ignoreCase = true) })
        }
    }

    override suspend fun buscarClientes(consulta: String): Flow<List<Cliente>> = flow {
        try {
            val response = clienteApiService.obtenerTodosLosClientes()
            if (response.isSuccessful) {
                val clientes = response.body() ?: emptyList()
                emit(clientes.filter {
                    it.nombre.contains(consulta, ignoreCase = true) ||
                    it.email.contains(consulta, ignoreCase = true) ||
                    it.telefono?.contains(consulta, ignoreCase = true) == true
                })
            } else {
                emit(clientesMock.filter {
                    it.nombre.contains(consulta, ignoreCase = true) ||
                    it.email.contains(consulta, ignoreCase = true) ||
                    it.telefono?.contains(consulta, ignoreCase = true) == true
                })
            }
        } catch (e: Exception) {
            emit(clientesMock.filter {
                it.nombre.contains(consulta, ignoreCase = true) ||
                it.email.contains(consulta, ignoreCase = true) ||
                it.telefono?.contains(consulta, ignoreCase = true) == true
            })
        }
    }

    override suspend fun autenticarAdministrador(
        email: String,
        codigoAcceso: String
    ): Flow<Cliente?> = flow {
        try {
            val response = clienteApiService.obtenerTodosLosClientes()
            if (response.isSuccessful) {
                val clientes = response.body() ?: emptyList()
                val administrador = clientes.find { cliente ->
                    cliente.rol == RolCliente.ADMIN && cliente.email.equals(email.trim(), ignoreCase = true)
                }
                val esCodigoValido = codigoAcceso.trim() == codigoAccesoAdministrador
                emit(if (administrador != null && esCodigoValido) administrador else null)
            } else {
                val administrador = clientesMock.find { cliente ->
                    cliente.rol == RolCliente.ADMIN && cliente.email.equals(email.trim(), ignoreCase = true)
                }
                val esCodigoValido = codigoAcceso.trim() == codigoAccesoAdministrador
                emit(if (administrador != null && esCodigoValido) administrador else null)
            }
        } catch (e: Exception) {
            val administrador = clientesMock.find { cliente ->
                cliente.rol == RolCliente.ADMIN && cliente.email.equals(email.trim(), ignoreCase = true)
            }
            val esCodigoValido = codigoAcceso.trim() == codigoAccesoAdministrador
            emit(if (administrador != null && esCodigoValido) administrador else null)
        }
    }

    override suspend fun insertarCliente(cliente: Cliente) {
        try {
            clienteApiService.crearCliente(cliente)
        } catch (e: Exception) {
            // Fallback to mock
            clientesMock.add(cliente)
        }
    }

    override suspend fun actualizarCliente(cliente: Cliente) {
        try {
            clienteApiService.actualizarCliente(cliente.id, cliente)
        } catch (e: Exception) {
            // Fallback to mock
            val index = clientesMock.indexOfFirst { it.id == cliente.id }
            if (index != -1) {
                clientesMock[index] = cliente
            }
        }
    }

    override suspend fun eliminarCliente(id: String) {
        try {
            clienteApiService.eliminarCliente(id)
        } catch (e: Exception) {
            // Fallback to mock
            clientesMock.removeAll { it.id == id }
        }
    }
}
