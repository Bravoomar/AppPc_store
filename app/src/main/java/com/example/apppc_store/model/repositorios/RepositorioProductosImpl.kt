package com.example.apppc_store.model.repositorios

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class RepositorioProductosImpl(private val context: Context) : RepositorioProductos {

    companion object {
        private const val TAG = "RepositorioProductosImpl"
        private const val LOCAL_FILE_NAME = "productos.json"
        private const val ROOT_DIR_NAME = "AppPc_store"
        private const val ROOT_FILE_NAME = "productos.json"
    }

    private val productosMock = mutableListOf<Producto>()

    init {
        try {
            val loaded = loadFromLocal()
            if (loaded.isNotEmpty()) {
                productosMock.addAll(loaded)
            } else {
                productosMock.addAll(defaultProductos())
                saveAll()
            }
        } catch (e: Exception) {
            Log.w(TAG, "Error cargando productos: ${e.message}")
            productosMock.addAll(defaultProductos())
        }
    }

    private fun defaultProductos() = listOf(
        Producto(
            id = "1",
            nombre = "Laptop Gaming",
            descripcion = "Laptop para gaming de alta gama",
            precio = 1500.0,
            categoria = "Tecnología",
            stock = 5,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "2",
            nombre = "Monitor Gaming 4K",
            descripcion = "Monitor ultra HD de 32 pulgadas para gaming profesional",
            precio = 800.0,
            categoria = "Tecnología",
            stock = 1,
            tipoProducto = TipoProducto.VENTA
        ),
        Producto(
            id = "3",
            nombre = "Smartphone",
            descripcion = "Teléfono inteligente de última generación",
            precio = 600.0,
            categoria = "Tecnología",
            stock = 10,
            tipoProducto = TipoProducto.VENTA
        )
    )

    override suspend fun obtenerTodosLosProductos(): Flow<List<Producto>> = flow {
        emit(productosMock.toList())
    }

    override suspend fun obtenerProductoPorId(id: String): Flow<Producto?> = flow {
        emit(productosMock.find { it.id == id })
    }

    override suspend fun obtenerProductosPorCategoria(categoriaId: String): Flow<List<Producto>> = flow {
        emit(productosMock.filter { it.categoria == categoriaId })
    }

    override suspend fun buscarProductos(consulta: String): Flow<List<Producto>> = flow {
        emit(productosMock.filter {
            it.nombre.contains(consulta, ignoreCase = true) ||
                it.descripcion.contains(consulta, ignoreCase = true)
        })
    }

    override suspend fun obtenerProductosEnVenta(): Flow<List<Producto>> = flow {
        emit(productosMock.filter { it.tipoProducto == TipoProducto.VENTA })
    }

    override suspend fun obtenerProductosEnArriendo(): Flow<List<Producto>> = flow {
        emit(productosMock.filter { it.tipoProducto == TipoProducto.ARRIENDO })
    }

    override suspend fun insertarProducto(producto: Producto) {
        productosMock.add(producto)
        saveAll()
    }

    override suspend fun actualizarProducto(producto: Producto) {
        val index = productosMock.indexOfFirst { it.id == producto.id }
        if (index != -1) {
            productosMock[index] = producto
            saveAll()
        }
    }

    override suspend fun eliminarProducto(id: String) {
        val removed = productosMock.removeAll { it.id == id }
        if (removed) saveAll()
    }

    override suspend fun actualizarStock(productoId: String, nuevoStock: Int) {
        val index = productosMock.indexOfFirst { it.id == productoId }
        if (index != -1) {
            val p = productosMock[index]
            productosMock[index] = p.copy(stock = nuevoStock, fechaActualizacion = System.currentTimeMillis())
            saveAll()
        }
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
            Log.w(TAG, "Fallo guardando en root/external: ${e.message}")
        }
    }

    private fun saveToLocal() {
        val jsonArray = JSONArray()
        for (p in productosMock) {
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("nombre", p.nombre)
            obj.put("descripcion", p.descripcion)
            obj.put("precio", p.precio)
            obj.put("categoria", p.categoria)
            obj.put("stock", p.stock)
            obj.put("imagenUrl", p.imagenUrl)
            obj.put("esActivo", p.esActivo)
            obj.put("tipoProducto", p.tipoProducto.name)
            obj.put("fechaCreacion", p.fechaCreacion)
            obj.put("fechaActualizacion", p.fechaActualizacion)
            jsonArray.put(obj)
        }
        val file = File(context.filesDir, LOCAL_FILE_NAME)
        file.writeText(jsonArray.toString())
    }

    private fun saveToRoot() {
        val external = Environment.getExternalStorageDirectory()
        val dir = File(external, ROOT_DIR_NAME)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, ROOT_FILE_NAME)
        val jsonArray = JSONArray()
        for (p in productosMock) {
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("nombre", p.nombre)
            obj.put("descripcion", p.descripcion)
            obj.put("precio", p.precio)
            obj.put("categoria", p.categoria)
            obj.put("stock", p.stock)
            obj.put("imagenUrl", p.imagenUrl)
            obj.put("esActivo", p.esActivo)
            obj.put("tipoProducto", p.tipoProducto.name)
            obj.put("fechaCreacion", p.fechaCreacion)
            obj.put("fechaActualizacion", p.fechaActualizacion)
            jsonArray.put(obj)
        }
        file.writeText(jsonArray.toString())
    }

    private fun loadFromLocal(): List<Producto> {
        val file = File(context.filesDir, LOCAL_FILE_NAME)
        if (!file.exists()) return emptyList()
        val text = file.readText()
        val arr = JSONArray(text)
        val out = mutableListOf<Producto>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            val producto = Producto(
                id = o.optString("id", java.util.UUID.randomUUID().toString()),
                nombre = o.optString("nombre", ""),
                descripcion = o.optString("descripcion", ""),
                precio = o.optDouble("precio", 0.0),
                categoria = o.optString("categoria", ""),
                stock = o.optInt("stock", 0),
                imagenUrl = if (o.has("imagenUrl")) o.optString("imagenUrl", null) else null,
                esActivo = o.optBoolean("esActivo", true),
                tipoProducto = try { TipoProducto.valueOf(o.optString("tipoProducto", TipoProducto.VENTA.name)) } catch (e: Exception) { TipoProducto.VENTA },
                fechaCreacion = o.optLong("fechaCreacion", System.currentTimeMillis()),
                fechaActualizacion = o.optLong("fechaActualizacion", System.currentTimeMillis())
            )
            out.add(producto)
        }
        return out
    }
}
