package com.example.tfg_movil.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.model.services.Service
import com.example.tfg_movil.viewmodel.ViewModelService

import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun ServiceScreen(viewModel: ViewModelService) {
    val servicios by viewModel.services.collectAsState()
    val editing by viewModel.editingService.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadServices() }
    Spacer(modifier = Modifier.height(20.dp))

    Column(Modifier.padding(16.dp)) {
        Text("Gestión de Servicios", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        ServiceForm(
            service = editing,
            onSave = { if (it.id == 0) viewModel.createService(it) else viewModel.updateService(it) },
            onCancel = viewModel::cancelEditing
        )

        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(servicios) { s ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${s.nombre} (${s.abreviatura})", style = MaterialTheme.typography.titleMedium)
                            Text("Color: ${s.color}")
                        }
                        Row {
                            IconButton(onClick = { viewModel.startEditing(s) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { viewModel.deleteService(s.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Borrar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceForm(
    service: Service?,
    onSave: (Service) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf(service?.nombre ?: "") }
    var abreviatura by remember { mutableStateOf(service?.abreviatura ?: "") }
    var color by remember { mutableStateOf(service?.color ?: "") }
    Spacer(modifier = Modifier.height(20.dp))

    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = abreviatura, onValueChange = { abreviatura = it }, label = { Text("Abreviatura") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") }, modifier = Modifier.fillMaxWidth())

        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            if (service != null) {
                TextButton(onClick = onCancel) { Text("Cancelar") }
            }
            Button(onClick = {
                if (nombre.isNotBlank() && abreviatura.isNotBlank() && color.isNotBlank()) {
                    onSave(Service(id = service?.id ?: 0, nombre = nombre, abreviatura = abreviatura, color = color))
                    nombre = ""; abreviatura = ""; color = ""
                }
            }) {
                Text(if (service != null) "Guardar cambios" else "Crear")
            }
        }
    }
}