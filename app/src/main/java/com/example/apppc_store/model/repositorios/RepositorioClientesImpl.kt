package com.example.apppc_store.model.repositorios

import com.example.apppc_store.api.ClienteApiService
import com.example.apppc_store.data.local.dao.ClienteDao
import com.example.apppc_store.data.local.entities.ClienteEntity
import com.example.apppc_store.model.entidades.Cliente
import com.example.apppc_store.model.entidades.RolCliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositorioClientesImpl @Inject constructor(
    private val clienteApiService: ClienteApiService,
    private val clienteDao: ClienteDao
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
            val clientesLocal = clienteDao.obtenerTodosLosClientes().first()
            if (clientesLocal.isEmpty()) {
                // Si no hay clientes en Room, insertar los mock
                clientesMock.forEach { cliente ->
                    clienteDao.insertarCliente(ClienteEntity.fromCliente(cliente))
                }
                emit(clientesMock)
            } else {
                emit(clientesLocal.map { it.toCliente() })
            }
        } catch (e: Exception) {
            emit(clientesMock)
        }
    }

    override suspend fun obtenerClientePorId(id: String): Flow<Cliente?> {
        return clienteDao.obtenerClientePorId(id).map { entity ->
            entity?.toCliente()
        }
    }

    override suspend fun obtenerClientePorEmail(email: String): Flow<Cliente?> {
        return clienteDao.obtenerClientePorEmail(email).map { entity ->
            entity?.toCliente()
        }
    }

    override suspend fun buscarClientes(consulta: String): Flow<List<Cliente>> {
        return clienteDao.buscarClientes(consulta).map { entities ->
            entities.map { it.toCliente() }
        }
    }

    override suspend fun autenticarAdministrador(
        email: String,
        codigoAcceso: String
    ): Flow<Cliente?> = flow {
        try {
            val clienteEntity = clienteDao.obtenerClientePorEmail(email.trim()).first()
            val cliente = clienteEntity?.toCliente()
            val esCodigoValido = codigoAcceso.trim() == codigoAccesoAdministrador
            val esAdministrador = cliente?.rol == RolCliente.ADMIN
            
            emit(if (cliente != null && esAdministrador && esCodigoValido) cliente else null)
        } catch (e: Exception) {
            // Fallback a mock si hay error
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
            // Si falla la API, guardar solo en local
        } finally {
            // Siempre guardar en Room
            clienteDao.insertarCliente(ClienteEntity.fromCliente(cliente))
        }
    }

    override suspend fun actualizarCliente(cliente: Cliente) {
        try {
            clienteApiService.actualizarCliente(cliente.id, cliente)
        } catch (e: Exception) {
            // Si falla la API, actualizar solo en local
        } finally {
            // Siempre actualizar en Room
            clienteDao.actualizarCliente(ClienteEntity.fromCliente(cliente))
        }
    }

    override suspend fun eliminarCliente(id: String) {
        try {
            clienteApiService.eliminarCliente(id)
        } catch (e: Exception) {
            // Si falla la API, eliminar solo en local
        } finally {
            // Siempre eliminar en Room
            clienteDao.eliminarCliente(id)
        }
    }
}
