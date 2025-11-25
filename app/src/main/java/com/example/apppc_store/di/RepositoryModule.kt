package com.example.apppc_store.di

import com.example.apppc_store.api.ClienteApiService
import com.example.apppc_store.api.ProductoApiService
import com.example.apppc_store.data.local.dao.ClienteDao
import com.example.apppc_store.data.local.dao.ProductoDao
import com.example.apppc_store.data.local.dao.VentaDao
import com.example.apppc_store.model.repositorios.RepositorioClientes
import com.example.apppc_store.model.repositorios.RepositorioClientesImpl
import com.example.apppc_store.model.repositorios.RepositorioProductos
import com.example.apppc_store.model.repositorios.RepositorioProductosImpl
import com.example.apppc_store.model.repositorios.RepositorioVentas
import com.example.apppc_store.model.repositorios.RepositorioVentasImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositorioProductos(
        repositorioProductosImpl: RepositorioProductosImpl
    ): RepositorioProductos

    @Binds
    @Singleton
    abstract fun bindRepositorioClientes(
        repositorioClientesImpl: RepositorioClientesImpl
    ): RepositorioClientes

    @Binds
    @Singleton
    abstract fun bindRepositorioVentas(
        repositorioVentasImpl: RepositorioVentasImpl
    ): RepositorioVentas
}

