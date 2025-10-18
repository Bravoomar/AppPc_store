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
import com.example.apppc_store.model.entidades.Cliente
import com.example.apppc_store.ui.componentes.*
import com.example.apppc_store.viewmodel.clientes.ClientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarCliente(
    cliente: Cliente,
    onVolver: () -> Unit,
    onClienteActualizado: (Cliente) -> Unit,
    viewModel: ClientesViewModel
) {
    var nombre by remember { mutableStateOf(cliente.nombre) }
    var email by remember { mutableStateOf(cliente.email) }
    var telefono by remember { mutableStateOf(cliente.telefono ?: "") }
    var direccion by remember { mutableStateOf(cliente.direccion ?: "") }
    
    var nombreError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var telefonoError by remember { mutableStateOf<String?>(null) }
    
    var estaEnviando by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf<String?>(null) }
    
    val estaCargando by viewModel.estaCargando.collectAsState()
    val error by viewModel.error.collectAsState()
    
    fun validarFormulario(): Boolean {
        nombreError = ValidacionFormulario.validarNombre(nombre)
        emailError = ValidacionFormulario.validarEmail(email)
        telefonoError = ValidacionFormulario.validarTelefono(telefono)
        
        return nombreError == null && emailError == null && telefonoError == null
    }
    
    fun actualizarCliente() {
        if (validarFormulario()) {
            estaEnviando = true
            val clienteActualizado = cliente.copy(
                nombre = nombre.trim(),
                email = email.trim(),
                telefono = telefono.trim(),
                direccion = direccion.trim().takeIf { it.isNotBlank() },
                fechaActualizacion = System.currentTimeMillis()
            )
            
            viewModel.actualizarCliente(clienteActualizado)
            mensajeExito = "Cliente actualizado exitosamente"
            estaEnviando = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cliente") },
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
                text = "Editar Información del Cliente",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            // Información del cliente actual
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Cliente ID: ${cliente.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Registrado: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(cliente.fechaCreacion))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Campo Nombre
            CampoTextoValidado(
                valor = nombre,
                onValorChange = { 
                    nombre = it
                    nombreError = null
                },
                etiqueta = "Nombre completo",
                error = nombreError,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )
            
            // Campo Email
            CampoEmail(
                email = email,
                onEmailChange = { 
                    email = it
                    emailError = null
                },
                error = emailError
            )
            
            // Campo Teléfono
            CampoTelefono(
                telefono = telefono,
                onTelefonoChange = { 
                    telefono = it
                    telefonoError = null
                },
                error = telefonoError
            )
            
            // Campo Dirección (opcional)
            CampoTextoValidado(
                valor = direccion,
                onValorChange = { direccion = it },
                etiqueta = "Dirección (opcional)",
                error = null,
                singleLine = false
            )
            
            // Mensaje de éxito
            val mensajeExitoActual = mensajeExito
            if (mensajeExitoActual != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = mensajeExitoActual,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            // Mensaje de error
            val errorActual = error
            if (errorActual != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = errorActual,
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
                    onClick = { actualizarCliente() },
                    modifier = Modifier.weight(1f),
                    enabled = !estaEnviando && !estaCargando
                ) {
                    if (estaEnviando || estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Actualizar")
                    }
                }
            }
        }
    }
}
