package com.example.apppc_store.data.local.dao

import androidx.room.*
import com.example.apppc_store.data.local.entities.ClienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Query("SELECT * FROM clientes ORDER BY fechaCreacion DESC")
    fun obtenerTodosLosClientes(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE id = :id")
    fun obtenerClientePorId(id: String): Flow<ClienteEntity?>

    @Query("SELECT * FROM clientes WHERE email = :email")
    fun obtenerClientePorEmail(email: String): Flow<ClienteEntity?>

    @Query("SELECT * FROM clientes WHERE nombre LIKE '%' || :consulta || '%' OR email LIKE '%' || :consulta || '%'")
    fun buscarClientes(consulta: String): Flow<List<ClienteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCliente(cliente: ClienteEntity)

    @Update
    suspend fun actualizarCliente(cliente: ClienteEntity)

    @Query("DELETE FROM clientes WHERE id = :id")
    suspend fun eliminarCliente(id: String)
}

