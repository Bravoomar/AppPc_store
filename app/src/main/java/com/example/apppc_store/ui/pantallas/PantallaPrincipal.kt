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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import com.example.apppc_store.model.entidades.Producto
import com.example.apppc_store.viewmodel.productos.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBarContent(onCarritoClick: () -> Unit) {
    var cartScale by remember { mutableStateOf(1f) }
    val animatedCartScale by animateFloatAsState(
        targetValue = cartScale,
        animationSpec = spring(dampingRatio = 0.3f),
        finishedListener = { if (cartScale != 1f) cartScale = 1f }
    )

    TopAppBar(
        title = { Text("Tienda AppPC") },
        actions = {
            IconButton(
                onClick = {
                    cartScale = 0.8f
                    onCarritoClick()
                },
                modifier = Modifier.scale(animatedCartScale)
            ) {
                Text("🛒")
            }
        }
    )
}

@Composable
private fun ActionButtons(
    onArriendosClick: () -> Unit,
    onAgregarProductoClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        var clientsScale by remember { mutableStateOf(1f) }
        val animatedClientsScale by animateFloatAsState(
            targetValue = clientsScale,
            animationSpec = spring(dampingRatio = 0.3f),
            finishedListener = { if (clientsScale != 1f) clientsScale = 1f }
        )

        var addScale by remember { mutableStateOf(1f) }
        val animatedAddScale by animateFloatAsState(
            targetValue = addScale,
            animationSpec = spring(dampingRatio = 0.3f),
            finishedListener = { if (addScale != 1f) addScale = 1f }
        )

        FloatingActionButton(
            onClick = {
                clientsScale = 0.8f
                onArriendosClick()
            },
            modifier = Modifier
                .scale(animatedClientsScale)
                .size(56.dp)
        ) {
            Text("👤")
        }

        FloatingActionButton(
            onClick = {
                addScale = 0.8f
                onAgregarProductoClick()
            },
            modifier = Modifier
                .scale(animatedAddScale)
                .size(56.dp)
        ) {
            Text("➕")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    onProductoClick: (Producto) -> Unit,
    onCarritoClick: () -> Unit,
    onArriendosClick: () -> Unit,
    onAgregarProductoClick: () -> Unit,
    viewModel: ProductosViewModel
) {
    val productos by viewModel.productos.collectAsState()
    val estaCargando by viewModel.estaCargando.collectAsState()
    val error by viewModel.error.collectAsState()

    // Animation states for buttons
    var cartScale by remember { mutableStateOf(1f) }
    val animatedCartScale by animateFloatAsState(
        targetValue = cartScale,
        animationSpec = spring(dampingRatio = 0.3f)
    )

    Scaffold(
        topBar = { AppBarContent(onCarritoClick = onCarritoClick) },
        floatingActionButton = { 
            ActionButtons(
                onArriendosClick = onArriendosClick,
                onAgregarProductoClick = onAgregarProductoClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Productos Destacados",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(modifier = Modifier.fillMaxSize()) {
                // Loading state
                AnimatedLoadingContent(visible = estaCargando)

                // Error state
                AnimatedErrorContent(visible = error != null && !estaCargando, errorMessage = error)

                // Empty state
                AnimatedEmptyContent(visible = productos.isEmpty() && !estaCargando && error == null)

                // Product list
                AnimatedProductList(
                    visible = productos.isNotEmpty() && !estaCargando && error == null,
                    productos = productos,
                    onProductoClick = onProductoClick
                )
            }
            }
        }
    }

@Composable
private fun AnimatedLoadingContent(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun AnimatedErrorContent(visible: Boolean, errorMessage: String?) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = errorMessage ?: "Error desconocido",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun AnimatedEmptyContent(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay productos disponibles")
        }
    }
}

@Composable
private fun AnimatedProductList(
    visible: Boolean,
    productos: List<Producto>,
    onProductoClick: (Producto) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = productos,
                key = { it.id }
            ) { producto ->
                var itemScale by remember { mutableStateOf(1f) }
                val animatedItemScale by animateFloatAsState(
                    targetValue = itemScale,
                    animationSpec = spring(dampingRatio = 0.3f),
                    finishedListener = { if (itemScale != 1f) itemScale = 1f }
                )

                TarjetaProducto(
                    producto = producto,
                    onClick = { 
                        itemScale = 0.95f
                        onProductoClick(producto)
                    },
                    modifier = Modifier.scale(animatedItemScale)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TarjetaProducto(
    producto: Producto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.3f),
        finishedListener = { if (pressed) pressed = false }
    )
    
    val stockColor by animateColorAsState(
        targetValue = when {
            producto.stock > 10 -> MaterialTheme.colorScheme.primary
            producto.stock > 5 -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.error
        },
        animationSpec = tween(durationMillis = 500)
    )
    
    var elevation by remember { mutableStateOf(1.dp) }
    val animatedElevation by animateDpAsState(
        targetValue = if (pressed) 8.dp else elevation,
        animationSpec = spring(dampingRatio = 0.3f)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .scale(scale)
            .animateContentSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        when {
                            event.type == PointerEventType.Enter -> elevation = 4.dp
                            event.type == PointerEventType.Exit -> elevation = 1.dp
                        }
                    }
                }
            },
        onClick = {
            pressed = true
            onClick()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.animateContentSize()
            )
            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.animateContentSize()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${producto.precio}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.animateContentSize()
                )
                AnimatedContent(
                    targetState = producto.stock,
                    transitionSpec = {
                        fadeIn() + slideInVertically() with 
                        fadeOut() + slideOutVertically()
                    }
                ) { stock ->
                    Text(
                        text = "Stock: $stock",
                        style = MaterialTheme.typography.bodySmall,
                        color = stockColor
                    )
                }
            }
        }
    }
}