package com.example.apppc_store.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ComponenteBusqueda(
    valor: String,
    onValorCambio: (String) -> Unit,
    placeholder: String = "Buscar productos...",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorCambio,
        placeholder = { Text(placeholder) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = {
            Text("🔍")
        }
    )
}
