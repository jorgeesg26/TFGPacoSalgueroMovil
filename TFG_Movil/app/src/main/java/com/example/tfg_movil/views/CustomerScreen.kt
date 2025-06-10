package com.example.tfg_movil.views

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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.model.customer.CustomerDTO
import com.example.tfg_movil.viewmodel.ViewModelCustomer
@Composable
fun CustomerScreen(viewModel: ViewModelCustomer) {
    val customers by viewModel.customers.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var paymentMethodId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadCustomers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Crear nuevo cliente", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))

        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        TextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") })
        TextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = paymentMethodId,
            onValueChange = { paymentMethodId = it },
            label = { Text("ID de método de pago") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = {
            if (paymentMethodId.toIntOrNull() != null) {
                val dto = CustomerDTO(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono,
                    email = email,
                    paymentMethodId = paymentMethodId.toInt()
                )
                viewModel.createCustomer(dto)
                nombre = ""; apellidos = ""; telefono = ""; email = ""; paymentMethodId = ""
            } else {
                Toast.makeText(context, "El método de pago debe ser un número", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Crear Cliente")
        }

        Spacer(Modifier.height(16.dp))

        if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        }

        Text("Lista de clientes", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(customers) { customer ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Nombre: ${customer.nombre} ${customer.apellidos}")
                        Text("Teléfono: ${customer.telefono}")
                        Text("Email: ${customer.email}")
                        Text("Método de pago: ${customer.paymentMethodId}")

                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = {
                                viewModel.updateCustomer(
                                    customer.id,
                                    customer.copy(nombre = "Actualizado")
                                )
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                viewModel.deleteCustomer(customer.id)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
