package com.example.tfg_movil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg_movil.model.authentication.DataStoreManager
import com.example.tfg_movil.model.services.Service
import com.example.tfg_movil.model.services.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelService(
    application: Application,
    private val repository: ServiceRepository
) : AndroidViewModel(application) {

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val context get() = getApplication<Application>().applicationContext

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _editingService = MutableStateFlow<Service?>(null)
    val editingService: StateFlow<Service?> = _editingService

    fun loadServices() {
        viewModelScope.launch {
            repository.fetchServices()
                .onSuccess { services ->
                    _services.value = services
                    _error.value = null
                }
                .onFailure { e ->
                    _error.value = "Error cargando servicios: ${e.message}"
                }
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            repository.createService(service)
                .onSuccess {
                    _error.value = "¡Servicio creado!"
                    loadServices()
                }
                .onFailure {
                    _error.value = "Error creando servicio: ${it.message}"
                }
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            repository.updateService(service.id, service)
                .onSuccess { updatedService ->
                    _services.value = _services.value.map {
                        if (it.id == service.id) updatedService else it
                    }
                    _editingService.value = null
                    _error.value = "Servicio actualizado"
                }
                .onFailure { e ->
                    _error.value = "Error actualizando: ${e.message}"
                }
        }
    }

    fun deleteService(id: Int) {
        viewModelScope.launch {
            val token = DataStoreManager.getAccessTokenSync(context) ?: ""
            repository.deleteService(id, token)
                .onSuccess {
                    _services.value = _services.value.filter { it.id != id }
                    _error.value = "Servicio eliminado correctamente"
                }
                .onFailure { e ->
                    _error.value = when {
                        e.message?.contains("404") == true -> "El servicio ya no existe"
                        else -> "Error al eliminar: ${e.message}"
                    }
                }
        }
    }

    fun startEditing(service: Service) {
        _editingService.value = service
    }

    fun cancelEditing() {
        _editingService.value = null
    }
}