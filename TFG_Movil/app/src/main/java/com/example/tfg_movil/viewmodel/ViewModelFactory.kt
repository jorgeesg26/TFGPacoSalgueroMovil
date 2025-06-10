package com.example.tfg_movil.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tfg_movil.model.services.ServiceRepository

class ViewModelFactory(
    private val application: Application,
    private val repository: ServiceRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ViewModelService(application, repository) as T
    }
}