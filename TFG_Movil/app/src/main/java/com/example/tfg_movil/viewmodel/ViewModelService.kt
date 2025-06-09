package com.example.tfg_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg_movil.model.services.Service
import com.example.tfg_movil.model.services.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelService(private val repository: ServiceRepository) : ViewModel() {
    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _editingService = MutableStateFlow<Service?>(null)
    val editingService: StateFlow<Service?> = _editingService

    fun loadServices() {
        viewModelScope.launch {
            repository.fetchServices()
                .onSuccess { _services.value = it }
                .onFailure { _error.value = it.message }
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            repository.createService(service)
                .onSuccess { loadServices() }
                .onFailure { _error.value = it.message }
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            repository.updateService(service.id, service)
                .onSuccess { loadServices(); _editingService.value = null }
                .onFailure { _error.value = it.message }
        }
    }

    fun deleteService(id: Int) {
        viewModelScope.launch {
            repository.deleteService(id)
                .onSuccess { loadServices() }
                .onFailure { _error.value = it.message }
        }
    }

    fun startEditing(service: Service) {
        _editingService.value = service
    }

    fun cancelEditing() {
        _editingService.value = null
    }
}