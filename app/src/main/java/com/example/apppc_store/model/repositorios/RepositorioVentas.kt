package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Venta
import kotlinx.coroutines.flow.Flow

interface RepositorioVentas {
    suspend fun obtenerTodasLasVentas(): Flow<List<Venta>>
    suspend fun obtenerVentaPorId(id: String): Flow<Venta?>
    suspend fun obtenerVentasPorCliente(clienteId: String): Flow<List<Venta>>
    suspend fun obtenerVentasPorEstado(estado: String): Flow<List<Venta>>
    suspend fun insertarVenta(venta: Venta)
    suspend fun actualizarVenta(venta: Venta)
    suspend fun eliminarVenta(id: String)
    suspend fun actualizarEstadoVenta(ventaId: String, nuevoEstado: String)
}
