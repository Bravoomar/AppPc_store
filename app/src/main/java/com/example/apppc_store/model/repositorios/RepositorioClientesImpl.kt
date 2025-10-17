package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Cliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RepositorioClientesImpl : RepositorioClientes {
    
    // Datos mock para pruebas
    private val clientesMock = mutableListOf(
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
        emit(clientesMock.toList())
    }

    override suspend fun obtenerClientePorId(id: String): Flow<Cliente?> = flow {
        emit(clientesMock.find { it.id == id })
    }

    override suspend fun obtenerClientePorEmail(email: String): Flow<Cliente?> = flow {
        emit(clientesMock.find { it.email.equals(email, ignoreCase = true) })
    }

    override suspend fun buscarClientes(consulta: String): Flow<List<Cliente>> = flow {
        emit(clientesMock.filter { 
            it.nombre.contains(consulta, ignoreCase = true) ||
            it.email.contains(consulta, ignoreCase = true) ||
            it.telefono.contains(consulta, ignoreCase = true)
        })
    }

    override suspend fun insertarCliente(cliente: Cliente) {
        // TODO: Implementar cuando se configure Room
        clientesMock.add(cliente)
    }

    override suspend fun actualizarCliente(cliente: Cliente) {
        // TODO: Implementar cuando se configure Room
        val index = clientesMock.indexOfFirst { it.id == cliente.id }
        if (index != -1) {
            clientesMock[index] = cliente
        }
    }

    override suspend fun eliminarCliente(id: String) {
        // TODO: Implementar cuando se configure Room
        clientesMock.removeAll { it.id == id }
    }
}
