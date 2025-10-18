package com.example.apppc_store.ui.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.apppc_store.model.entidades.Cliente
import com.example.apppc_store.ui.componentes.*
import com.example.apppc_store.viewmodel.clientes.ClientesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistroCliente(
    onVolver: () -> Unit,
    onClienteRegistrado: (Cliente) -> Unit,
    viewModel: ClientesViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    
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
    
    fun registrarCliente() {
        if (validarFormulario()) {
            estaEnviando = true
            val cliente = Cliente(
                id = System.currentTimeMillis().toString(),
                nombre = nombre.trim(),
                email = email.trim(),
                telefono = telefono.trim(),
                direccion = direccion.trim().takeIf { it.isNotBlank() }
            )
            
            viewModel.agregarCliente(cliente)
            mensajeExito = "Cliente registrado exitosamente"
            estaEnviando = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Cliente") },
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
                text = "Información del Cliente",
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
                    onClick = { registrarCliente() },
                    modifier = Modifier.weight(1f),
                    enabled = !estaEnviando && !estaCargando
                ) {
                    if (estaEnviando || estaCargando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Registrar")
                    }
                }
            }
        }
    }
}
