package com.example.apppc_store.model

import com.example.apppc_store.model.entidades.CarritoCompras
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import org.junit.Assert.*
import org.junit.Test

class CarritoComprasTest {

    @Test
    fun `agregarItem debería agregar producto al carrito`() {
        // Given
        val carrito = CarritoCompras()
        val producto = Producto(
            id = "1",
            nombre = "Monitor 160Hz",
            descripcion = "Monitor gaming",
            precio = 450.0,
            categoria = "Monitores",
            stock = 8,
            tipoProducto = TipoProducto.VENTA
        )

        // When
        val nuevoCarrito = carrito.agregarItem(producto, 2)

        // Then
        assertEquals(1, nuevoCarrito.items.size)
        assertEquals(2, nuevoCarrito.items[0].cantidad)
        assertEquals(900.0, nuevoCarrito.montoTotal, 0.01)
    }

    @Test
    fun `agregarItem no debería exceder el stock disponible`() {
        // Given
        val carrito = CarritoCompras()
        val producto = Producto(
            id = "1",
            nombre = "Monitor 160Hz",
            descripcion = "Monitor gaming",
            precio = 450.0,
            categoria = "Monitores",
            stock = 5,
            tipoProducto = TipoProducto.VENTA
        )

        // When
        val nuevoCarrito = carrito.agregarItem(producto, 10) // Intenta agregar más del stock

        // Then
        assertEquals(1, nuevoCarrito.items.size)
        assertEquals(5, nuevoCarrito.items[0].cantidad) // Solo debe agregar el stock disponible
    }

    @Test
    fun `eliminarItem debería eliminar producto del carrito`() {
        // Given
        val producto = Producto(
            id = "1",
            nombre = "Monitor 160Hz",
            descripcion = "Monitor gaming",
            precio = 450.0,
            categoria = "Monitores",
            stock = 8,
            tipoProducto = TipoProducto.VENTA
        )
        val carrito = CarritoCompras().agregarItem(producto, 2)

        // When
        val nuevoCarrito = carrito.eliminarItem("1")

        // Then
        assertEquals(0, nuevoCarrito.items.size)
        assertEquals(0.0, nuevoCarrito.montoTotal, 0.01)
    }

    @Test
    fun `actualizarCantidad debería actualizar la cantidad del item`() {
        // Given
        val producto = Producto(
            id = "1",
            nombre = "Kit Gamer",
            descripcion = "Kit completo",
            precio = 320.0,
            categoria = "Periféricos",
            stock = 12,
            tipoProducto = TipoProducto.VENTA
        )
        val carrito = CarritoCompras().agregarItem(producto, 3)

        // When
        val nuevoCarrito = carrito.actualizarCantidad("1", 5)

        // Then
        assertEquals(5, nuevoCarrito.items[0].cantidad)
        assertEquals(1600.0, nuevoCarrito.montoTotal, 0.01)
    }

    @Test
    fun `montoTotal debería calcular correctamente el total`() {
        // Given
        val producto1 = Producto(
            id = "1",
            nombre = "Monitor 160Hz",
            descripcion = "Monitor gaming",
            precio = 450.0,
            categoria = "Monitores",
            stock = 8,
            tipoProducto = TipoProducto.VENTA
        )
        val producto2 = Producto(
            id = "2",
            nombre = "Kit Gamer",
            descripcion = "Kit completo",
            precio = 320.0,
            categoria = "Periféricos",
            stock = 12,
            tipoProducto = TipoProducto.VENTA
        )
        var carrito = CarritoCompras()
        carrito = carrito.agregarItem(producto1, 2)
        carrito = carrito.agregarItem(producto2, 1)

        // When
        val total = carrito.montoTotal

        // Then
        assertEquals(1220.0, total, 0.01) // (450 * 2) + (320 * 1) = 1220
    }
}

