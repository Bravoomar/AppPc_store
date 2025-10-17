package com.example.apppc_store.model.repositorios

import com.example.apppc_store.model.entidades.Categoria
import kotlinx.coroutines.flow.Flow

interface RepositorioCategorias {
    suspend fun obtenerTodasLasCategorias(): Flow<List<Categoria>>
    suspend fun obtenerCategoriaPorId(id: String): Flow<Categoria?>
    suspend fun obtenerCategoriasActivas(): Flow<List<Categoria>>
    suspend fun insertarCategoria(categoria: Categoria)
    suspend fun actualizarCategoria(categoria: Categoria)
    suspend fun eliminarCategoria(id: String)
}
