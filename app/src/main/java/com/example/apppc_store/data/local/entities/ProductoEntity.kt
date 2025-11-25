package com.example.apppc_store.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val stock: Int,
    val imagenUrl: String? = null,
    val esActivo: Boolean = true,
    val tipoProducto: String = TipoProducto.VENTA.name,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaActualizacion: Long = System.currentTimeMillis()
) {
    fun toProducto(): Producto {
        return Producto(
            id = id,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            categoria = categoria,
            stock = stock,
            imagenUrl = imagenUrl,
            esActivo = esActivo,
            tipoProducto = TipoProducto.valueOf(tipoProducto),
            fechaCreacion = fechaCreacion,
            fechaActualizacion = fechaActualizacion
        )
    }

    companion object {
        fun fromProducto(producto: Producto): ProductoEntity {
            return ProductoEntity(
                id = producto.id,
                nombre = producto.nombre,
                descripcion = producto.descripcion,
                precio = producto.precio,
                categoria = producto.categoria,
                stock = producto.stock,
                imagenUrl = producto.imagenUrl,
                esActivo = producto.esActivo,
                tipoProducto = producto.tipoProducto.name,
                fechaCreacion = producto.fechaCreacion,
                fechaActualizacion = producto.fechaActualizacion
            )
        }
    }
}

