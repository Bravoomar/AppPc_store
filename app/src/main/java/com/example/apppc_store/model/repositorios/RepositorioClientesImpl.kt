package com.example.apppc_store.model.repositorios

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.apppc_store.model.entidades.Cliente
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class RepositorioClientesImpl(private val context: Context) : RepositorioClientes {

    companion object {
        private const val TAG = "RepositorioClientesImpl"
        private const val LOCAL_FILE_NAME = "clientes.json"
        private const val ROOT_DIR_NAME = "AppPc_store"
        private const val ROOT_FILE_NAME = "clientes.json"
    }

    // Datos en memoria (se cargan desde almacenamiento cuando sea posible)
    private val clientesMock = mutableListOf<Cliente>()

    init {
        // Intentar cargar desde almacenamiento local al iniciar
        try {
            val loaded = loadFromLocal()
            if (loaded.isNotEmpty()) {
                clientesMock.addAll(loaded)
            } else {
                // Si no hay datos, crear algunos mocks por defecto
                clientesMock.addAll(defaultClientes())
                // Guardar inmediatamente para persistencia
                saveAll()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error cargando clientes: ${e.message}")
            clientesMock.addAll(defaultClientes())
        }
    }

    private fun defaultClientes(): List<Cliente> = listOf(
        Cliente(
            id = "1",
            nombre = "Juan Pérez",
            email = "juan.perez@email.com",
            telefono = "123456789",
            direccion = "Calle Principal 123"
        ),
        Cliente(
            id = "2",
            nombre = "María García",
            email = "maria.garcia@email.com",
            telefono = "987654321",
            direccion = "Avenida Central 456"
        )
    )

    override suspend fun obtenerTodosLosClientes(): Flow<List<Cliente>> = flow {
        emit(clientesMock.toList())
    }

    override suspend fun obtenerClientePorId(id: String): Flow<Cliente?> = flow {
        emit(clientesMock.find { it.id == id })
    }

    override suspend fun obtenerClientePorEmail(email: String): Flow<Cliente?> = flow {
        emit(clientesMock.find { it.email.equals(email, ignoreCase = true) })
    }

    override suspend fun buscarClientes(consulta: String): Flow<List<Cliente>> = flow {
        emit(clientesMock.filter {
            it.nombre.contains(consulta, ignoreCase = true) ||
                it.email.contains(consulta, ignoreCase = true) ||
                (it.telefono?.contains(consulta, ignoreCase = true) == true)
        })
    }

    override suspend fun insertarCliente(cliente: Cliente) {
        clientesMock.add(cliente)
        saveAll()
    }

    override suspend fun actualizarCliente(cliente: Cliente) {
        val index = clientesMock.indexOfFirst { it.id == cliente.id }
        if (index != -1) {
            clientesMock[index] = cliente
            saveAll()
        }
    }

    override suspend fun eliminarCliente(id: String) {
        val removed = clientesMock.removeAll { it.id == id }
        if (removed) saveAll()
    }

    private fun saveAll() {
        try {
            saveToLocal()
        } catch (e: Exception) {
            Log.w(TAG, "Fallo guardando localmente: ${e.message}")
        }

        try {
            saveToRoot()
        } catch (e: Exception) {
            // Guardar a root puede fallar en Android recientes/dependiendo permisos
            Log.w(TAG, "Fallo guardando en root/external: ${e.message}")
        }
    }

    private fun saveToLocal() {
        val jsonArray = JSONArray()
        for (c in clientesMock) {
            val obj = JSONObject()
            obj.put("id", c.id)
            obj.put("nombre", c.nombre)
            obj.put("email", c.email)
            obj.put("telefono", c.telefono)
            obj.put("direccion", c.direccion)
            obj.put("esActivo", c.esActivo)
            obj.put("fechaCreacion", c.fechaCreacion)
            obj.put("fechaActualizacion", c.fechaActualizacion)
            jsonArray.put(obj)
        }
        val file = File(context.filesDir, LOCAL_FILE_NAME)
        file.writeText(jsonArray.toString())
    }

    private fun saveToRoot() {
        // Intentar escribir en la raíz del almacenamiento externo bajo una carpeta de la app
        val external = Environment.getExternalStorageDirectory()
        val dir = File(external, ROOT_DIR_NAME)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, ROOT_FILE_NAME)
        val jsonArray = JSONArray()
        for (c in clientesMock) {
            val obj = JSONObject()
            obj.put("id", c.id)
            obj.put("nombre", c.nombre)
            obj.put("email", c.email)
            obj.put("telefono", c.telefono)
            obj.put("direccion", c.direccion)
            obj.put("esActivo", c.esActivo)
            obj.put("fechaCreacion", c.fechaCreacion)
            obj.put("fechaActualizacion", c.fechaActualizacion)
            jsonArray.put(obj)
        }
        file.writeText(jsonArray.toString())
    }

    private fun loadFromLocal(): List<Cliente> {
        val file = File(context.filesDir, LOCAL_FILE_NAME)
        if (!file.exists()) return emptyList()
        val text = file.readText()
        val arr = JSONArray(text)
        val out = mutableListOf<Cliente>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val cliente = Cliente(
                id = o.optString("id", java.util.UUID.randomUUID().toString()),
                nombre = o.optString("nombre", ""),
                email = o.optString("email", ""),
                telefono = if (o.has("telefono")) o.optString("telefono", null) else null,
                direccion = if (o.has("direccion")) o.optString("direccion", null) else null,
                esActivo = o.optBoolean("esActivo", true),
                fechaCreacion = o.optLong("fechaCreacion", System.currentTimeMillis()),
                fechaActualizacion = o.optLong("fechaActualizacion", System.currentTimeMillis())
            )
            out.add(cliente)
        }
        return out
    }
}
