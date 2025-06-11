package com.example.tfg_movil.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.viewmodel.ViewModelAgenda

@Composable
fun AgendaScreen(viewModel: ViewModelAgenda) {
    val entradas by viewModel.entradas.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarTodas()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(Modifier.height(60.dp))
        Text("Agenda Profesional", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(entradas) { entrada ->
                Card(modifier = Modifier.padding(8.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Fecha: ${entrada.fechaHora}")
                        Text("Cliente: ${entrada.cliente}")
                        Text("Centro: ${entrada.centroTrabajo}")
                        Text("Servicio: ${entrada.service?.nombre ?: entrada.serviceId}")
                        Text("Precio: €${entrada.precio}")
                        entrada.paciente?.let { Text("Paciente: $it") }
                        entrada.observaciones?.let { Text("Observaciones: $it") }
                    }
                }
            }
        }

        if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        }
    }
}
