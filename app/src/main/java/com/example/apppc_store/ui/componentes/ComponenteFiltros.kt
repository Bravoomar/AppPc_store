package com.example.apppc_store.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apppc_store.model.entidades.TipoProducto

@Composable
fun ComponenteFiltros(
    filtroSeleccionado: TipoProducto?,
    onFiltroSeleccionado: (TipoProducto?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filtros = listOf(
        null to "Todos",
        TipoProducto.VENTA to "Venta"
    )

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filtros) { (tipo, nombre) ->
            FilterChip(
                onClick = { onFiltroSeleccionado(tipo) },
                label = { Text(nombre) },
                selected = filtroSeleccionado == tipo
            )
        }
    }
}
