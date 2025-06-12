package com.example.tfg_movil.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.R
import com.example.tfg_movil.model.paymentMethod.PaymentMethod
import com.example.tfg_movil.viewmodel.ViewModelPaymentMethod

@Composable
fun PaymentMethodScreen(viewModel: ViewModelPaymentMethod) {
    val context = LocalContext.current

    val methods by viewModel.methods.collectAsState()
    val editing by viewModel.editing.collectAsState()
    val error by viewModel.error.collectAsState()

    var method by remember(editing) { mutableStateOf(editing?.method ?: "") }
    var installments by remember(editing) { mutableStateOf(editing?.installments?.toString() ?: "") }
    var firstPaymentDays by remember(editing) { mutableStateOf(editing?.firstPaymentDays?.toString() ?: "") }
    var daysBetweenPayments by remember(editing) { mutableStateOf(editing?.daysBetweenPayments?.toString() ?: "") }

    LaunchedEffect(Unit) {
        viewModel.loadMethods()
    }

    LaunchedEffect(editing) {
        method = editing?.method ?: ""
        installments = editing?.installments?.toString() ?: ""
        firstPaymentDays = editing?.firstPaymentDays?.toString() ?: ""
        daysBetweenPayments = editing?.daysBetweenPayments?.toString() ?: ""
    }

    Text(stringResource(id = R.string.crearMetodosPago))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(69.dp))

            Text(
                text = if (editing != null) stringResource(id = R.string.editarMetodoPago) else stringResource(id = R.string.crearMetodoPago),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = method,
                onValueChange = { method = it },
                label = { Text(stringResource(id = R.string.metodo)) },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = installments,
                onValueChange = { installments = it },
                label = { Text(stringResource(id = R.string.cuotas)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = firstPaymentDays,
                onValueChange = { firstPaymentDays = it },
                label = { Text(stringResource(id = R.string.diasHastaPrimerPago)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = daysBetweenPayments,
                onValueChange = { daysBetweenPayments = it },
                label = { Text(stringResource(id = R.string.diasEntrePagos)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val installmentsNum = installments.toIntOrNull()
                    val firstNum = firstPaymentDays.toIntOrNull()
                    val betweenNum = daysBetweenPayments.toIntOrNull()

                    if (installmentsNum != null && firstNum != null && betweenNum != null) {
                        val methodObj = PaymentMethod(
                            id = editing?.id ?: 0,
                            method = method,
                            installments = installmentsNum,
                            firstPaymentDays = firstNum,
                            daysBetweenPayments = betweenNum
                        )

                        if (editing != null) {
                            viewModel.update(methodObj)
                        } else {
                            viewModel.create(methodObj)
                        }

                        method = ""
                        installments = ""
                        firstPaymentDays = ""
                        daysBetweenPayments = ""
                    } else {
                        Toast.makeText(context, "Todos los campos deben ser válidos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editing != null) stringResource(id = R.string.GuardarCambios)
                    else stringResource(id = R.string.crearMetodoPago))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (error != null) {
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(stringResource(id = R.string.listaMetodosPago), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(methods) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${stringResource(R.string.metodo)}: ${item.method}")
                    Text("${stringResource(R.string.cuotas)}: ${item.installments}")
                    Text("${stringResource(R.string.diasHastaPrimerPago)}: ${item.firstPaymentDays}")
                    Text("${stringResource(R.string.diasEntrePagos)}: ${item.daysBetweenPayments}")

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { viewModel.startEditing(item) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = {
                            if (item.id != 0) {
                                viewModel.delete(item.id)
                            } else {
                                Toast.makeText(context, "ID inválido", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }
}