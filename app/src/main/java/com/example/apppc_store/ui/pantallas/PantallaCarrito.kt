package com.example.apppc_store.ui.pantallas

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apppc_store.model.entidades.ItemCarrito
import com.example.apppc_store.viewmodel.ventas.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(
    onConfirmarCompra: () -> Unit,
    onVolver: () -> Unit,
    viewModel: CarritoViewModel
) {
    val carrito by viewModel.carrito.collectAsState()
    val estaCargando by viewModel.estaCargando.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Text("←")
                    }
                }
            )
        },
        bottomBar = {
            if (carrito.items.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${viewModel.obtenerMontoTotal()}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onConfirmarCompra,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Confirmar Compra")
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
                .padding(16.dp)
        ) {
            when {
                estaCargando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                error != null -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error ?: "Error desconocido",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                carrito.items.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🛒",
                                style = MaterialTheme.typography.displayLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Tu carrito está vacío",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Agrega algunos productos para comenzar",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = carrito.items,
                            key = { it.productoId }  // Key for proper animations
                        ) { item ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                TarjetaItemCarrito(
                                    item = item,
                                    onActualizarCantidad = { nuevaCantidad ->
                                        viewModel.actualizarCantidad(item.productoId, nuevaCantidad)
                                    },
                                    onEliminar = {
                                        viewModel.eliminarDelCarrito(item.productoId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaItemCarrito(
    item: ItemCarrito,
    onActualizarCantidad: (Int) -> Unit,
    onEliminar: () -> Unit
) {
    var isRemoving by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .scale(animateFloatAsState(
                targetValue = if (isRemoving) 0.8f else 1f,
                animationSpec = tween(durationMillis = 200)
            ).value)
            .alpha(animateFloatAsState(
                targetValue = if (isRemoving) 0f else 1f,
                animationSpec = tween(durationMillis = 200)
            ).value)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nombreProducto,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${item.precio} c/u",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Total: $${item.precioTotal}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón decrementar con animación
                var isPressingMinus by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { onActualizarCantidad(item.cantidad - 1) },
                    modifier = Modifier.scale(
                        animateFloatAsState(
                            targetValue = if (isPressingMinus) 0.8f else 1f,
                            animationSpec = tween(durationMillis = 100)
                        ).value
                    )
                ) {
                    Text("-")
                }
                
                // Cantidad con animación
                val cantidadTransition = updateTransition(item.cantidad, label = "cantidad")
                val cantidadScale by cantidadTransition.animateFloat(
                    transitionSpec = { spring(stiffness = Spring.StiffnessLow) },
                    label = "scale"
                ) { 
                    if (it == item.cantidad) 1f else 0.8f 
                }
                Box(
                    modifier = Modifier.scale(cantidadScale),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.cantidad.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
                
                // Botón incrementar con animación
                var isPressingPlus by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { onActualizarCantidad(item.cantidad + 1) },
                    modifier = Modifier.scale(
                        animateFloatAsState(
                            targetValue = if (isPressingPlus) 0.8f else 1f,
                            animationSpec = tween(durationMillis = 100)
                        ).value
                    )
                ) {
                    Text("+")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Botón eliminar con animación
                var isPressingDelete by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { 
                        isRemoving = true
                        onEliminar()
                    },
                    modifier = Modifier.scale(
                        animateFloatAsState(
                            targetValue = if (isPressingDelete) 0.8f else 1f,
                            animationSpec = tween(durationMillis = 100)
                        ).value
                    )
                ) {
                    Text("🗑️")
                }
            }
        }
    }
}
