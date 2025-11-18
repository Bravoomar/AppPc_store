package com.example.apppc_store.ui.navegacion

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.ui.pantallas.PantallaCarrito
import com.example.apppc_store.ui.pantallas.PantallaDetalleProducto
import com.example.apppc_store.ui.pantallas.PantallaPrincipal
import com.example.apppc_store.ui.pantallas.PantallaRegistroCliente
import com.example.apppc_store.ui.pantallas.PantallaAgregarProducto
import com.example.apppc_store.ui.pantallas.PantallaEditarCliente
import com.example.apppc_store.viewmodel.productos.ProductosViewModel
import com.example.apppc_store.viewmodel.ventas.CarritoViewModel
import com.example.apppc_store.viewmodel.clientes.ClientesViewModel

@Composable
fun NavegacionPrincipal(
    navController: NavHostController = rememberNavController(),
    productosViewModel: ProductosViewModel,
    carritoViewModel: CarritoViewModel,
    clientesViewModel: ClientesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "principal"
    ) {
        composable("principal") {
            PantallaPrincipal(
                onProductoClick = { producto ->
                    navController.navigate("detalle/${producto.id}")
                },
                onCarritoClick = {
                    navController.navigate("carrito")
                },
                onArriendosClick = {
                    navController.navigate("registro_cliente")
                },
                onAgregarProductoClick = {
                    navController.navigate("agregar_producto")
                },
                viewModel = productosViewModel
            )
        }
        
        composable("detalle/{productoId}") { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId") ?: ""
            // Obtener producto por ID desde ViewModel
            val producto = productosViewModel.productos.value.find { it.id == productoId } ?: Producto(
                id = productoId,
                nombre = "Producto no encontrado",
                descripcion = "No se pudo cargar el producto",
                precio = 0.0,
                categoria = "Sin categoría",
                stock = 0
            )
            
            PantallaDetalleProducto(
                producto = producto,
                onVolver = {
                    navController.popBackStack()
                },
                viewModel = carritoViewModel
            )
        }
        
        composable("carrito") {
            PantallaCarrito(
                onConfirmarCompra = {
                    // TODO: Implementar confirmación de compra
                    navController.navigate("principal")
                },
                onVolver = {
                    navController.popBackStack()
                },
                viewModel = carritoViewModel
            )
        }
        
        composable("registro_cliente") {
            PantallaRegistroCliente(
                onVolver = {
                    navController.popBackStack()
                },
                onClienteRegistrado = { cliente ->
                    // TODO: Manejar cliente registrado
                    navController.popBackStack()
                },
                viewModel = clientesViewModel
            )
        }
        
        composable("agregar_producto") {
            PantallaAgregarProducto(
                onVolver = {
                    navController.popBackStack()
                },
                onProductoAgregado = { producto ->
                    // TODO: Manejar producto agregado
                    navController.popBackStack()
                },
                viewModel = productosViewModel
            )
        }
        
        composable("editar_cliente/{clienteId}") { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId") ?: ""
            // TODO: Obtener cliente por ID desde ViewModel
            val cliente = com.example.apppc_store.model.entidades.Cliente(
                id = clienteId,
                nombre = "Cliente de Ejemplo",
                email = "cliente@ejemplo.com",
                telefono = "123456789"
            )
            
            PantallaEditarCliente(
                cliente = cliente,
                onVolver = {
                    navController.popBackStack()
                },
                onClienteActualizado = { clienteActualizado ->
                    // TODO: Manejar cliente actualizado
                    navController.popBackStack()
                },
                viewModel = clientesViewModel
            )
        }
    }
}
