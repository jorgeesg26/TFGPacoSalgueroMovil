package com.example.tfg_movil.model.customer

data class CustomerDTO(
    val id: Int = 0, // opcional en creación
    val cif: Int,
    val name: String,
    val adress: String,
    val postalCode: Int,
    val placeOfResidence: String,
    val phoneNumber: Int,
    val email: String,
    val adminEmail: String,
    val paymentMethodId: Int
)
