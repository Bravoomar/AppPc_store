package com.example.apppc_store.viewmodel.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppc_store.model.repositorios.RepositorioProductos

class ProductosViewModelFactory(
    private val repositorioProductos: RepositorioProductos
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            return ProductosViewModel(repositorioProductos) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

