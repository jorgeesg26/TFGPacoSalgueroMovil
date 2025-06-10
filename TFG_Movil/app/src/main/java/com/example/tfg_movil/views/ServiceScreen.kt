package com.example.tfg_movil.views

import android.app.Application
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tfg_movil.model.authentication.classes.RetrofitInstance
import com.example.tfg_movil.model.services.Service
import com.example.tfg_movil.viewmodel.ViewModelFactory
import com.example.tfg_movil.viewmodel.ViewModelService
import com.example.tfg_movil.model.services.ServiceRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
@Composable
fun ServiceScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: ViewModelService = viewModel(
        factory = ViewModelFactory(application, ServiceRepository(RetrofitInstance.serviceClient))
    )
    val services by viewModel.services.collectAsState()
    val editingService by viewModel.editingService.collectAsState()
    val error by viewModel.error.collectAsState()

    var nombre by remember(editingService) {
        mutableStateOf(editingService?.nombre ?: "")
    }
    var abreviatura by remember(editingService) {
        mutableStateOf(editingService?.abreviatura ?: "")
    }
    var color by remember(editingService) {
        mutableStateOf(editingService?.color ?: "")
    }

    LaunchedEffect(Unit) {
        viewModel.loadServices()
    }

    LaunchedEffect(editingService) {
        nombre = editingService?.nombre ?: ""
        abreviatura = editingService?.abreviatura ?: ""
        color = editingService?.color ?: ""
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Spacer(Modifier.height(100.dp))

            Text(
                text = if (editingService != null) "Editar servicio" else "Crear nuevo servicio",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = abreviatura,
                onValueChange = { abreviatura = it },
                label = { Text("Abreviatura") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = color,
                onValueChange = { color = it },
                label = { Text("Color") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val nuevoServicio = Service(
                        id = editingService?.id ?: 0,
                        nombre = nombre,
                        abreviatura = abreviatura,
                        color = color
                    )

                    if (editingService != null) {
                        viewModel.updateService(nuevoServicio)
                    } else {
                        viewModel.createService(nuevoServicio)
                    }

                    nombre = ""; abreviatura = ""; color = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editingService != null) "Guardar cambios" else "Crear servicio")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }

            Text("Lista de servicios", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(services) { service ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Nombre: ${service.nombre}")
                    Text("Abreviatura: ${service.abreviatura}")
                    Text("Color: ${service.color}")
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            viewModel.startEditing(service)
                            println("edit clicked")
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            if (service.id != 0) {
                                viewModel.deleteService(service.id)
                            } else {
                                Toast.makeText(context, "ID inválido", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            println("borrao clicked")
                        }
                    }
                }
            }
        }
    }
}
