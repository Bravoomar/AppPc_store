package com.example.apppc_store.ui

import com.example.apppc_store.ui.componentes.ValidacionFormulario
import org.junit.Assert.*
import org.junit.Test

class ValidacionFormularioTest {

    @Test
    fun `validarEmail debería retornar null para email válido`() {
        // Given
        val emailValido = "test@example.com"

        // When
        val resultado = ValidacionFormulario.validarEmail(emailValido)

        // Then
        assertNull(resultado)
    }

    @Test
    fun `validarEmail debería retornar error para email inválido`() {
        // Given
        val emailInvalido = "email-invalido"

        // When
        val resultado = ValidacionFormulario.validarEmail(emailInvalido)

        // Then
        assertNotNull(resultado)
        assertTrue(resultado!!.contains("inválido"))
    }

    @Test
    fun `validarEmail debería retornar error para email vacío`() {
        // Given
        val emailVacio = ""

        // When
        val resultado = ValidacionFormulario.validarEmail(emailVacio)

        // Then
        assertNotNull(resultado)
        assertTrue(resultado!!.contains("requerido"))
    }

    @Test
    fun `validarTelefono debería retornar null para teléfono válido`() {
        // Given
        val telefonoValido = "123456789"

        // When
        val resultado = ValidacionFormulario.validarTelefono(telefonoValido)

        // Then
        assertNull(resultado)
    }

    @Test
    fun `validarTelefono debería retornar error para teléfono muy corto`() {
        // Given
        val telefonoCorto = "123"

        // When
        val resultado = ValidacionFormulario.validarTelefono(telefonoCorto)

        // Then
        assertNotNull(resultado)
        assertTrue(resultado!!.contains("8 dígitos"))
    }

    @Test
    fun `validarNombre debería retornar null para nombre válido`() {
        // Given
        val nombreValido = "Juan Pérez"

        // When
        val resultado = ValidacionFormulario.validarNombre(nombreValido)

        // Then
        assertNull(resultado)
    }

    @Test
    fun `validarNombre debería retornar error para nombre muy corto`() {
        // Given
        val nombreCorto = "A"

        // When
        val resultado = ValidacionFormulario.validarNombre(nombreCorto)

        // Then
        assertNotNull(resultado)
        assertTrue(resultado!!.contains("2 caracteres"))
    }

    @Test
    fun `validarPrecio debería retornar null para precio válido`() {
        // Given
        val precioValido = "99.99"

        // When
        val resultado = ValidacionFormulario.validarPrecio(precioValido)

        // Then
        assertNull(resultado)
    }

    @Test
    fun `validarPrecio debería retornar error para precio negativo`() {
        // Given
        val precioNegativo = "-10"

        // When
        val resultado = ValidacionFormulario.validarPrecio(precioNegativo)

        // Then
        assertNotNull(resultado)
        assertTrue(resultado!!.contains("mayor a 0"))
    }

    @Test
    fun `validarStock debería retornar null para stock válido`() {
        // Given
        val stockValido = "10"

        // When
        val resultado = ValidacionFormulario.validarStock(stockValido)

        // Then
        assertNull(resultado)
    }

    @Test
    fun `validarStock debería retornar error para stock negativo`() {
        // Given
        val stockNegativo = "-5"

        // When
        val resultado = ValidacionFormulario.validarStock(stockNegativo)

        // Then
        assertNotNull(resultado)
        assertTrue(resultado!!.contains("negativo"))
    }
}

