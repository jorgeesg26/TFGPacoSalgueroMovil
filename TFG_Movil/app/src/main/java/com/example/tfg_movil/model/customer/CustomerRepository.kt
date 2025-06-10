package com.example.tfg_movil.model.customer

import com.example.tfg_movil.model.authentication.classes.RetrofitInstance

class CustomerRepository {
    private val api = RetrofitInstance.customerClient

    suspend fun fetchCustomers() = runCatching { api.getAllCustomers() }

    suspend fun createCustomer(dto: CustomerDTO) = runCatching { api.createCustomer(dto) }

    suspend fun updateCustomer(id: Int, customer: Customer) = runCatching { api.updateCustomer(id, customer) }

    suspend fun deleteCustomer(id: Int) = runCatching { api.deleteCustomer(id) }
}

