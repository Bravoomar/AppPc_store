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
import com.example.apppc_store.ui.navegacion.NavegacionPrincipal
import com.example.apppc_store.ui.theme.AppPc_storeTheme
import com.example.apppc_store.viewmodel.productos.ProductosViewModel
import com.example.apppc_store.viewmodel.ventas.CarritoViewModel
import com.example.apppc_store.viewmodel.clientes.ClientesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                    // Crear ViewModels usando Hilt
                    val productosViewModel: ProductosViewModel = viewModel()
                    val carritoViewModel: CarritoViewModel = viewModel()
                    val clientesViewModel: ClientesViewModel = viewModel()

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
