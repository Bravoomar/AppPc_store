package com.example.apppc_store.data.local.dao

import androidx.room.*
import com.example.apppc_store.data.local.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos WHERE esActivo = 1 ORDER BY fechaCreacion DESC")
    fun obtenerTodosLosProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    fun obtenerProductoPorId(id: String): Flow<ProductoEntity?>

    @Query("SELECT * FROM productos WHERE categoria = :categoria AND esActivo = 1")
    fun obtenerProductosPorCategoria(categoria: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE (nombre LIKE '%' || :consulta || '%' OR descripcion LIKE '%' || :consulta || '%') AND esActivo = 1")
    fun buscarProductos(consulta: String): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE tipoProducto = 'VENTA' AND esActivo = 1")
    fun obtenerProductosEnVenta(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE tipoProducto = 'ARRIENDO' AND esActivo = 1")
    fun obtenerProductosEnArriendo(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: ProductoEntity)

    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)

    @Query("UPDATE productos SET stock = :nuevoStock, fechaActualizacion = :timestamp WHERE id = :productoId")
    suspend fun actualizarStock(productoId: String, nuevoStock: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE productos SET esActivo = 0 WHERE id = :id")
    suspend fun eliminarProducto(id: String)

    @Query("DELETE FROM productos")
    suspend fun eliminarTodosLosProductos()
}

