package com.example.apppc_store.viewmodel.clientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppc_store.model.repositorios.RepositorioClientes

class ClientesViewModelFactory(
    private val repositorioClientes: RepositorioClientes
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientesViewModel::class.java)) {
            return ClientesViewModel(repositorioClientes) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

