package com.example.apppc_store.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apppc_store.model.entidades.Cliente
import com.example.apppc_store.model.entidades.RolCliente

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val email: String,
    val telefono: String? = null,
    val direccion: String? = null,
    val esActivo: Boolean = true,
    val rol: String = RolCliente.CLIENTE.name,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fechaActualizacion: Long = System.currentTimeMillis()
) {
    fun toCliente(): Cliente {
        return Cliente(
            id = id,
            nombre = nombre,
            email = email,
            telefono = telefono,
            direccion = direccion,
            esActivo = esActivo,
            rol = RolCliente.valueOf(rol),
            fechaCreacion = fechaCreacion,
            fechaActualizacion = fechaActualizacion
        )
    }

    companion object {
        fun fromCliente(cliente: Cliente): ClienteEntity {
            return ClienteEntity(
                id = cliente.id,
                nombre = cliente.nombre,
                email = cliente.email,
                telefono = cliente.telefono,
                direccion = cliente.direccion,
                esActivo = cliente.esActivo,
                rol = cliente.rol.name,
                fechaCreacion = cliente.fechaCreacion,
                fechaActualizacion = cliente.fechaActualizacion
            )
        }
    }
}

