package com.example.apppc_store.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apppc_store.data.local.dao.ClienteDao
import com.example.apppc_store.data.local.dao.ProductoDao
import com.example.apppc_store.data.local.dao.VentaDao
import com.example.apppc_store.data.local.entities.ClienteEntity
import com.example.apppc_store.data.local.entities.ProductoEntity
import com.example.apppc_store.data.local.entities.VentaEntity

@Database(
    entities = [
        ProductoEntity::class,
        ClienteEntity::class,
        VentaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun clienteDao(): ClienteDao
    abstract fun ventaDao(): VentaDao
}

