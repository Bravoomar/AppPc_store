package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Arriendo
import kotlinx.coroutines.flow.Flow

interface RepositorioArriendos {
    suspend fun obtenerTodosLosArriendos(): Flow<List<Arriendo>>
    suspend fun obtenerArriendoPorId(id: String): Flow<Arriendo?>
    suspend fun obtenerArriendosPorCliente(clienteId: String): Flow<List<Arriendo>>
    suspend fun obtenerArriendosPorEstado(estado: String): Flow<List<Arriendo>>
    suspend fun obtenerArriendosActivos(): Flow<List<Arriendo>>
    suspend fun insertarArriendo(arriendo: Arriendo)
    suspend fun actualizarArriendo(arriendo: Arriendo)
    suspend fun eliminarArriendo(id: String)
    suspend fun actualizarEstadoArriendo(arriendoId: String, nuevoEstado: String)
}
