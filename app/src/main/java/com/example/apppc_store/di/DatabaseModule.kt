package com.example.apppc_store.di

import android.content.Context
import androidx.room.Room
import com.example.apppc_store.data.local.AppDatabase
import com.example.apppc_store.data.local.dao.ProductoDao
import com.example.apppc_store.data.local.dao.ClienteDao
import com.example.apppc_store.data.local.dao.VentaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "apppc_store_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideProductoDao(database: AppDatabase): ProductoDao {
        return database.productoDao()
    }

    @Provides
    @Singleton
    fun provideClienteDao(database: AppDatabase): ClienteDao {
        return database.clienteDao()
    }

    @Provides
    @Singleton
    fun provideVentaDao(database: AppDatabase): VentaDao {
        return database.ventaDao()
    }
}

