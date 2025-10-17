package com.example.apppc_store.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.model.entidades.TipoProducto
import com.example.apppc_store.ui.componentes.*
import com.example.apppc_store.viewmodel.productos.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarProducto(
    onVolver: () -> Unit,
    onProductoAgregado: (Producto) -> Unit,
    viewModel: ProductosViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var tipoProducto by remember { mutableStateOf(TipoProducto.VENTA) }
    
    var nombreError by remember { mutableStateOf<String?>(null) }
    var descripcionError by remember { mutableStateOf<String?>(null) }
    var precioError by remember { mutableStateOf<String?>(null) }
    var categoriaError by remember { mutableStateOf<String?>(null) }
    var stockError by remember { mutableStateOf<String?>(null) }
    
    var estaEnviando by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf<String?>(null) }
    
    val estaCargando by viewModel.estaCargando.collectAsState()
    val error by viewModel.error.collectAsState()
    
    fun validarFormulario(): Boolean {
        nombreError = ValidacionFormulario.validarNombre(nombre)
        descripcionError = ValidacionFormulario.validarDescripcion(descripcion)
        precioError = ValidacionFormulario.validarPrecio(precio)
        categoriaError = if (categoria.isBlank()) "La categoría es requerida" else null
        stockError = ValidacionFormulario.validarStock(stock)
        
        return nombreError == null && descripcionError == null && 
               precioError == null && categoriaError == null && stockError == null
    }
    
    fun agregarProducto() {
        if (validarFormulario()) {
            estaEnviando = true
            val producto = Producto(
                id = System.currentTimeMillis().toString(),
                nombre = nombre.trim(),
                descripcion = descripcion.trim(),
                precio = precio.toDouble(),
                categoria = categoria.trim(),
                stock = stock.toInt(),
                tipoProducto = tipoProducto
            )
            
            viewModel.agregarProducto(producto)
            mensajeExito = "Producto agregado exitosamente"
            estaEnviando = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Text("←")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Text(
                text = "Información del Producto",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            // Campo Nombre
            CampoTextoValidado(
                valor = nombre,
                onValorChange = { 
                    nombre = it
                    nombreError = null
                },
                etiqueta = "Nombre del producto",
                error = nombreError,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            
            // Campo Descripción
            CampoDescripcion(
                descripcion = descripcion,
                onDescripcionChange = { 
                    descripcion = it
                    descripcionError = null
                },
                error = descripcionError
            )
            
            // Campo Precio
            CampoPrecio(
                precio = precio,
                onPrecioChange = { 
                    precio = it
                    precioError = null
                },
                error = precioError
            )
            
            // Campo Categoría
            CampoTextoValidado(
                valor = categoria,
                onValorChange = { 
                    categoria = it
                    categoriaError = null
                },
                etiqueta = "Categoría",
                error = categoriaError,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            
            // Campo Stock
            CampoStock(
                stock = stock,
                onStockChange = { 
                    stock = it
                    stockError = null
                },
                error = stockError
            )
            
            // Selector de Tipo de Producto
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tipo de Producto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            onClick = { tipoProducto = TipoProducto.VENTA },
                            label = { Text("Venta") },
                            selected = tipoProducto == TipoProducto.VENTA,
                            modifier = Modifier.weight(1f)
                        )
                        
                        FilterChip(
                            onClick = { tipoProducto = TipoProducto.ARRIENDO },
                            label = { Text("Arriendo") },
                            selected = tipoProducto == TipoProducto.ARRIENDO,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Mensaje de éxito
            if (mensajeExito != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = mensajeExito,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Mensaje de error
            if (error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                
                Button(
                    onClick = { agregarProducto() },
                    modifier = Modifier.weight(1f),
                    enabled = !estaEnviando && !estaCargando
                ) {
                    if (estaEnviando || estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Agregar")
                    }
                }
            }
        }
    }
}
