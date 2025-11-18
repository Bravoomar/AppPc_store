package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Cliente
import kotlinx.coroutines.flow.Flow

interface RepositorioClientes {
    suspend fun obtenerTodosLosClientes(): Flow<List<Cliente>>
    suspend fun obtenerClientePorId(id: String): Flow<Cliente?>
    suspend fun obtenerClientePorEmail(email: String): Flow<Cliente?>
    suspend fun buscarClientes(consulta: String): Flow<List<Cliente>>
    suspend fun autenticarAdministrador(email: String, codigoAcceso: String): Flow<Cliente?>
    suspend fun insertarCliente(cliente: Cliente)
    suspend fun actualizarCliente(cliente: Cliente)
    suspend fun eliminarCliente(id: String)
}
