package com.example.tfg_movil.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.viewmodel.ViewModelService

@Composable
fun ServiceScreen(viewModel: ViewModelService) {
    val servicios by viewModel.services.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadServices()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (error != null) {
            Text("Error: $error")
        } else {
            servicios.forEach { service ->
                Text("Nombre: ${service.nombre}")
                Text("Abreviatura: ${service.abreviatura}")
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
