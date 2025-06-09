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
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var abreviatura by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadServices()
    }
    Spacer(modifier = Modifier.height(100.dp))

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = abreviatura, onValueChange = { abreviatura = it }, label = { Text("Abreviatura") })
        TextField(value = color, onValueChange = { color = it }, label = { Text("Color") })

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            viewModel.createService(Service(nombre = nombre, abreviatura = abreviatura, color = color))
            nombre = ""; abreviatura = ""; color = ""
        }) {
            Text("Crear servicio")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(servicios) { s ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                ) {
                    Column {
                        Text(text = s.nombre, style = MaterialTheme.typography.bodyLarge)
                        Text(text = s.abreviatura)
                        Text(text = s.color)
                    }
                    Row {
                        IconButton(onClick = {
                            viewModel.updateService(s.id, s.copy(nombre = "Editado"))
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            viewModel.deleteService(s.id)
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar")
                        }
                    }
                }
            }
        }
    }
}


