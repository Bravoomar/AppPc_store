package com.example.apppc_store.viewmodel

import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import com.example.apppc_store.model.repositorios.RepositorioProductos
import com.example.apppc_store.viewmodel.productos.ProductosViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class ProductosViewModelTest {

    @Mock
    private lateinit var repositorioProductos: RepositorioProductos

    private lateinit var viewModel: ProductosViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = ProductosViewModel(repositorioProductos)
    }
    
    @Test
    fun `test inicializacion`() {
        // Verificar que el ViewModel se inicializa correctamente
        assertNotNull(viewModel)
    }

    @Test
    fun `cargarProductos debería cargar productos correctamente`() = runTest {
        // Given
        val productos = listOf(
            Producto(
                id = "1",
                nombre = "Monitor 160Hz",
                descripcion = "Monitor gaming",
                precio = 450.0,
                categoria = "Monitores",
                stock = 8,
                tipoProducto = TipoProducto.VENTA
            )
        )
        whenever(repositorioProductos.obtenerTodosLosProductos()).thenReturn(flowOf(productos))

        // When
        viewModel.cargarProductos()

        // Then
        val productosCargados = viewModel.productos.value
        assert(productosCargados.isNotEmpty())
        assert(productosCargados[0].nombre == "Monitor 160Hz")
    }

    @Test
    fun `seleccionarProducto debería establecer producto seleccionado`() = runTest {
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

        // When
        viewModel.seleccionarProducto(producto)

        // Then
        assert(viewModel.productoSeleccionado.value == producto)
    }
}

