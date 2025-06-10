package com.example.tfg_movil.model.navigation

sealed class RutasNavegacion(val route: String) {
    object Main : RutasNavegacion("Main")
    object Login : RutasNavegacion("Login")
    object Register: RutasNavegacion("Register")
    object Servicios : RutasNavegacion("Servicios")
    object Customer : RutasNavegacion("Customer")

}