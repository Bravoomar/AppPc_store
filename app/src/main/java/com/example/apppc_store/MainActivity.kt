package com.example.apppc_store

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppc_store.model.repositorios.RepositorioProductosImpl
import com.example.apppc_store.ui.navegacion.NavegacionPrincipal
import com.example.apppc_store.ui.theme.AppPc_storeTheme
import com.example.apppc_store.viewmodel.productos.ProductosViewModel
import com.example.apppc_store.viewmodel.productos.ProductosViewModelFactory
import com.example.apppc_store.viewmodel.ventas.CarritoViewModel
import com.example.apppc_store.viewmodel.clientes.ClientesViewModel
import com.example.apppc_store.viewmodel.clientes.ClientesViewModelFactory
import com.example.apppc_store.model.repositorios.RepositorioClientesImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPc_storeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Crear repositorios
                    val repositorioProductos = RepositorioProductosImpl()
                    val repositorioClientes = RepositorioClientesImpl()
                    
                    // Crear ViewModels usando viewModel() con ViewModelFactory
                    val productosViewModel: ProductosViewModel = viewModel(
                        factory = ProductosViewModelFactory(repositorioProductos)
                    )
                    val carritoViewModel: CarritoViewModel = viewModel()
                    val clientesViewModel: ClientesViewModel = viewModel(
                        factory = ClientesViewModelFactory(repositorioClientes)
                    )
                    
                    NavegacionPrincipal(
                        productosViewModel = productosViewModel,
                        carritoViewModel = carritoViewModel,
                        clientesViewModel = clientesViewModel
                    )
                }
            }
        }
    }
}