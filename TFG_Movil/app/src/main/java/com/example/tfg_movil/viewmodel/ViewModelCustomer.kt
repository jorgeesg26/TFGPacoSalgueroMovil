package com.example.tfg_movil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfg_movil.model.customer.Customer
import com.example.tfg_movil.model.customer.CustomerDTO
import com.example.tfg_movil.model.customer.CustomerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class ViewModelCustomer(private val repository: CustomerRepository) : ViewModel() {

    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCustomers() {
        viewModelScope.launch {
            repository.fetchCustomers()
                .onSuccess { _customers.value = it }
                .onFailure { _error.value = it.message }
        }
     }

    fun createCustomer(dto: CustomerDTO) {
        viewModelScope.launch {
            repository.createCustomer(dto)
                .onSuccess { loadCustomers() }
                .onFailure { _error.value = it.message }
        }
    }

    fun updateCustomer(id: Int, customer: Customer) {
        viewModelScope.launch {
            repository.updateCustomer(id, customer)
                .onSuccess { loadCustomers() }
                .onFailure { _error.value = it.message }
        }
    }

    fun deleteCustomer(id: Int) {
        viewModelScope.launch {
            repository.deleteCustomer(id)
                .onSuccess { loadCustomers() }
                .onFailure { _error.value = it.message }
        }
    }
}
