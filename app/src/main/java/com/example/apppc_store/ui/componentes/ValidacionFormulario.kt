package com.example.apppc_store.ui.componentes

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Utilidades para validación de formularios
 */
object ValidacionFormulario {
    
    fun validarEmail(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato de email inválido"
            else -> null
        }
    }
    
    fun validarTelefono(telefono: String): String? {
        return when {
            telefono.isBlank() -> "El teléfono es requerido"
            telefono.length < 8 -> "El teléfono debe tener al menos 8 dígitos"
            !telefono.all { it.isDigit() || it == '+' || it == '-' || it == ' ' } -> "Formato de teléfono inválido"
            else -> null
        }
    }
    
    fun validarNombre(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            !nombre.all { it.isLetter() || it == ' ' } -> "El nombre solo puede contener letras"
            else -> null
        }
    }
    
    fun validarPrecio(precio: String): String? {
        return when {
            precio.isBlank() -> "El precio es requerido"
            precio.toDoubleOrNull() == null -> "Formato de precio inválido"
            precio.toDouble() <= 0 -> "El precio debe ser mayor a 0"
            else -> null
        }
    }
    
    fun validarStock(stock: String): String? {
        return when {
            stock.isBlank() -> "El stock es requerido"
            stock.toIntOrNull() == null -> "Formato de stock inválido"
            stock.toInt() < 0 -> "El stock no puede ser negativo"
            else -> null
        }
    }
    
    fun validarDescripcion(descripcion: String): String? {
        return when {
            descripcion.isBlank() -> "La descripción es requerida"
            descripcion.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            else -> null
        }
    }
}

/**
 * Campo de texto con validación
 */
@Composable
fun CampoTextoValidado(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    modificador: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = error != null,
        supportingText = if (error != null) { { Text(error) } } else null,
        modifier = modificador,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        enabled = enabled,
        singleLine = singleLine
    )
}

/**
 * Campo de texto para email
 */
@Composable
fun CampoEmail(
    email: String,
    onEmailChange: (String) -> Unit,
    error: String?,
    modificador: Modifier = Modifier,
    enabled: Boolean = true
) {
    CampoTextoValidado(
        valor = email,
        onValorChange = onEmailChange,
        etiqueta = "Email",
        error = error,
        modificador = modificador,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        enabled = enabled
    )
}

/**
 * Campo de texto para teléfono
 */
@Composable
fun CampoTelefono(
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    error: String?,
    modificador: Modifier = Modifier,
    enabled: Boolean = true
) {
    CampoTextoValidado(
        valor = telefono,
        onValorChange = onTelefonoChange,
        etiqueta = "Teléfono",
        error = error,
        modificador = modificador,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        enabled = enabled
    )
}

/**
 * Campo de texto para precio
 */
@Composable
fun CampoPrecio(
    precio: String,
    onPrecioChange: (String) -> Unit,
    error: String?,
    modificador: Modifier = Modifier,
    enabled: Boolean = true
) {
    CampoTextoValidado(
        valor = precio,
        onValorChange = onPrecioChange,
        etiqueta = "Precio ($)",
        error = error,
        modificador = modificador,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        enabled = enabled
    )
}

/**
 * Campo de texto para stock
 */
@Composable
fun CampoStock(
    stock: String,
    onStockChange: (String) -> Unit,
    error: String?,
    modificador: Modifier = Modifier,
    enabled: Boolean = true
) {
    CampoTextoValidado(
        valor = stock,
        onValorChange = onStockChange,
        etiqueta = "Stock",
        error = error,
        modificador = modificador,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        enabled = enabled
    )
}

/**
 * Campo de texto para descripción (multilínea)
 */
@Composable
fun CampoDescripcion(
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    error: String?,
    modificador: Modifier = Modifier,
    enabled: Boolean = true
) {
    CampoTextoValidado(
        valor = descripcion,
        onValorChange = onDescripcionChange,
        etiqueta = "Descripción",
        error = error,
        modificador = modificador,
        singleLine = false,
        enabled = enabled
    )
}
