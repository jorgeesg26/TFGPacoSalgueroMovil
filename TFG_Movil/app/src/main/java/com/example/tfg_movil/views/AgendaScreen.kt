package com.example.tfg_movil.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.R
import com.example.tfg_movil.model.agenda.EntradaAgenda
import com.example.tfg_movil.viewmodel.ViewModelAgenda
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Composable
fun AgendaScreen(viewModel: ViewModelAgenda) {
    val entradas by viewModel.entradas.collectAsState()
    val error by viewModel.error.collectAsState()

    var cliente by remember { mutableStateOf("") }
    var centroTrabajo by remember { mutableStateOf("") }
    var serviceId by remember { mutableStateOf("") }
    var paciente by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarTodas()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Spacer(Modifier.height(100.dp))

            Text(stringResource(id = R.string.crearNuevaEntrada), style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            TextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text(stringResource(id = R.string.cliente)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = centroTrabajo,
                onValueChange = { centroTrabajo = it },
                label = { Text(stringResource(id = R.string.centroTrabajo)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = serviceId,
                onValueChange = { serviceId = it },
                label = { Text(stringResource(id = R.string.idServicio)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = paciente,
                onValueChange = { paciente = it },
                label = { Text(stringResource(id = R.string.paciente)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text(stringResource(id = R.string.precio)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = observaciones,
                onValueChange = { observaciones = it },
                label = { Text(stringResource(id = R.string.observaciones)) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = fechaHora,
                onValueChange = { fechaHora = it },
                label = { Text(stringResource(id = R.string.fechaHora)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    try {
                        val parsedFecha = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault()).parse(fechaHora)
                        // 2025-06-15T10:30
                        if (
                            parsedFecha != null &&
                            serviceId.toIntOrNull() != null &&
                            precio.toDoubleOrNull() != null
                        ) {
                            val entrada = EntradaAgenda(
                                fechaHora = parsedFecha,
                                cliente = cliente,
                                centroTrabajo = centroTrabajo,
                                serviceId = serviceId.toInt(),
                                paciente = paciente.takeIf { it.isNotBlank() },
                                precio = precio.toDouble(),
                                observaciones = observaciones.takeIf { it.isNotBlank() }
                            )
                            viewModel.crearEntrada(entrada)

                            cliente = ""
                            centroTrabajo = ""
                            serviceId = ""
                            paciente = ""
                            precio = ""
                            observaciones = ""
                            fechaHora = ""

                            Toast.makeText(context, "Entrada creada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Campos numéricos o fecha inválidos", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.crearNuevaEntrada))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }

            Text(stringResource(id = R.string.listaEntradas), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(entradas) { entrada ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${stringResource(R.string.fechaHora)}: ${entrada.fechaHora}")
                    Text("${stringResource(R.string.cliente)}: ${entrada.cliente}")
                    Text("${stringResource(R.string.centroTrabajo)}: ${entrada.centroTrabajo}")
                    Text("${stringResource(R.string.servicio)}: ${entrada.service?.nombre ?: entrada.serviceId}")
                    Text("${stringResource(R.string.precio)}: €${entrada.precio}")
                    entrada.paciente?.let { Text("${stringResource(R.string.paciente)}: $it") }
                    entrada.observaciones?.let { Text("${stringResource(R.string.observaciones)}: $it") }
                }
            }
        }
    }
}
