package com.example.tfg_movil.model.customer

data class CustomerDTO(
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    val email: String,
    val paymentMethodId: Int
)
