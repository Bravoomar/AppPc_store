package com.example.apppc_store.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.viewmodel.ventas.CarritoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleProducto(
    producto: Producto,
    onVolver: () -> Unit,
    viewModel: CarritoViewModel
) {
    var cantidad by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Text("←")
                    }
                }
            )
        },
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Selector de cantidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Cantidad:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        IconButton(
                            onClick = { if (cantidad > 1) cantidad-- },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("-")
                        }
                        OutlinedTextField(
                            value = cantidad.toString(),
                            onValueChange = { newValue ->
                                val nuevaCantidad = newValue.toIntOrNull() ?: 1
                                if (nuevaCantidad > 0 && nuevaCantidad <= producto.stock) {
                                    cantidad = nuevaCantidad
                                }
                            },
                            modifier = Modifier.width(80.dp),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                            textStyle = androidx.compose.ui.text.TextStyle(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        )
                        IconButton(
                            onClick = { if (cantidad < producto.stock) cantidad++ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("+")
                        }
                    }

                    // Botón de agregar al carrito con animación
                    var isAddingToCart by remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isAddingToCart) 0.95f else 1f,
                        animationSpec = spring(dampingRatio = 0.7f)
                    )
                    val checkAlpha by animateFloatAsState(
                        targetValue = if (isAddingToCart) 1f else 0f,
                        animationSpec = tween(durationMillis = 300)
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    isAddingToCart = true
                                    viewModel.agregarAlCarrito(producto, cantidad)
                                    delay(800) // Show success animation
                                    isAddingToCart = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .scale(buttonScale)
                        ) {
                            AnimatedVisibility(
                                visible = !isAddingToCart,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Text("Agregar al Carrito")
                            }
                            AnimatedVisibility(
                                visible = isAddingToCart,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Text("✓")
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen del producto (placeholder)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🖼️",
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

            // Información del producto
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = producto.descripcion,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Precio",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$${producto.precio}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Stock",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = producto.stock.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Tipo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Venta",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Categoría: ${producto.categoria}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
