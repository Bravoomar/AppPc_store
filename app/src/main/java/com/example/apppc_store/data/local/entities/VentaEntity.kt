package com.example.apppc_store.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apppc_store.model.entidades.EstadoVenta
import com.example.apppc_store.model.entidades.ItemVenta
import com.example.apppc_store.model.entidades.Venta
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "ventas")
data class VentaEntity(
    @PrimaryKey
    val id: String,
    val clienteId: String,
    val montoTotal: Double,
    val estado: String = EstadoVenta.PENDIENTE.name,
    val fechaVenta: Long = System.currentTimeMillis(),
    val notas: String? = null,
    val itemsJson: String = "[]" // JSON string de los items
) {
    fun toVenta(gson: Gson = Gson()): Venta {
        val itemsType = object : TypeToken<List<ItemVenta>>() {}.type
        val items = gson.fromJson<List<ItemVenta>>(itemsJson, itemsType) ?: emptyList()
        
        return Venta(
            id = id,
            clienteId = clienteId,
            items = items,
            montoTotal = montoTotal,
            estado = EstadoVenta.valueOf(estado),
            fechaVenta = fechaVenta,
            notas = notas
        )
    }

    companion object {
        fun fromVenta(venta: Venta, gson: Gson = Gson()): VentaEntity {
            val itemsJson = gson.toJson(venta.items)
            
            return VentaEntity(
                id = venta.id,
                clienteId = venta.clienteId,
                montoTotal = venta.montoTotal,
                estado = venta.estado.name,
                fechaVenta = venta.fechaVenta,
                notas = venta.notas,
                itemsJson = itemsJson
            )
        }
    }
}

