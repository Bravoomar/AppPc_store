package com.example.apppc_store.data.local.dao

import androidx.room.*
import com.example.apppc_store.data.local.entities.VentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {
    @Query("SELECT * FROM ventas ORDER BY fechaVenta DESC")
    fun obtenerTodasLasVentas(): Flow<List<VentaEntity>>

    @Query("SELECT * FROM ventas WHERE id = :id")
    fun obtenerVentaPorId(id: String): Flow<VentaEntity?>

    @Query("SELECT * FROM ventas WHERE clienteId = :clienteId")
    fun obtenerVentasPorCliente(clienteId: String): Flow<List<VentaEntity>>

    @Query("SELECT * FROM ventas WHERE estado = :estado")
    fun obtenerVentasPorEstado(estado: String): Flow<List<VentaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVenta(venta: VentaEntity)

    @Update
    suspend fun actualizarVenta(venta: VentaEntity)

    @Query("UPDATE ventas SET estado = :nuevoEstado WHERE id = :ventaId")
    suspend fun actualizarEstadoVenta(ventaId: String, nuevoEstado: String)

    @Query("DELETE FROM ventas WHERE id = :id")
    suspend fun eliminarVenta(id: String)
}

