package com.example.apppc_store

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.apppc_store.model.repositorios.RepositorioProductosImpl
import com.example.apppc_store.ui.navegacion.NavegacionPrincipal
import com.example.apppc_store.ui.theme.AppPc_storeTheme
import com.example.apppc_store.viewmodel.productos.ProductosViewModel
import com.example.apppc_store.viewmodel.ventas.CarritoViewModel
import com.example.apppc_store.viewmodel.clientes.ClientesViewModel
import com.example.apppc_store.model.repositorios.RepositorioClientesImpl

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Si se concede, los repositorios intentarán escribir en external/root.
        // Si no, seguirán guardando en almacenamiento interno.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Solicitar permiso de escritura en almacenamiento externo si es necesario
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            when (ContextCompat.checkSelfPermission(this, permission)) {
                PackageManager.PERMISSION_GRANTED -> {
                    // ya concedido
                }
                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        }

        setContent {
            AppPc_storeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Crear repositorios pasando el Context
                    val repositorioProductos = RepositorioProductosImpl(this)
                    val repositorioClientes = RepositorioClientesImpl(this)
                    val productosViewModel = ProductosViewModel(repositorioProductos)
                    val carritoViewModel = CarritoViewModel()
                    val clientesViewModel = ClientesViewModel(repositorioClientes)

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