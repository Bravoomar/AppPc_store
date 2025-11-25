package com.example.apppc_store.model.repositorios

import com.example.apppc_store.data.local.dao.VentaDao
import com.example.apppc_store.data.local.entities.VentaEntity
import com.example.apppc_store.model.entidades.Venta
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositorioVentasImpl @Inject constructor(
    private val ventaDao: VentaDao,
    private val gson: Gson
) : RepositorioVentas {

    override suspend fun obtenerTodasLasVentas(): Flow<List<Venta>> {
        return ventaDao.obtenerTodasLasVentas().map { entities ->
            entities.map { it.toVenta(gson) }
        }
    }

    override suspend fun obtenerVentaPorId(id: String): Flow<Venta?> {
        return ventaDao.obtenerVentaPorId(id).map { entity ->
            entity?.toVenta(gson)
        }
    }

    override suspend fun obtenerVentasPorCliente(clienteId: String): Flow<List<Venta>> {
        return ventaDao.obtenerVentasPorCliente(clienteId).map { entities ->
            entities.map { it.toVenta(gson) }
        }
    }

    override suspend fun obtenerVentasPorEstado(estado: String): Flow<List<Venta>> {
        return ventaDao.obtenerVentasPorEstado(estado).map { entities ->
            entities.map { it.toVenta(gson) }
        }
    }

    override suspend fun insertarVenta(venta: Venta) {
        ventaDao.insertarVenta(VentaEntity.fromVenta(venta, gson))
    }

    override suspend fun actualizarVenta(venta: Venta) {
        ventaDao.actualizarVenta(VentaEntity.fromVenta(venta, gson))
    }

    override suspend fun eliminarVenta(id: String) {
        ventaDao.eliminarVenta(id)
    }

    override suspend fun actualizarEstadoVenta(ventaId: String, nuevoEstado: String) {
        ventaDao.actualizarEstadoVenta(ventaId, nuevoEstado)
    }
}

