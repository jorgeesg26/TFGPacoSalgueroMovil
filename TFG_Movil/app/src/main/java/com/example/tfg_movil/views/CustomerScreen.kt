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

    var cif by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var adress by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var placeOfResidence by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var adminEmail by remember { mutableStateOf("") }
    var paymentMethodId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadCustomers()
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Spacer(Modifier.height(69.dp))
            Text("Crear nuevo cliente", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))

            TextField(value = cif, onValueChange = { cif = it }, label = { Text("CIF") })
            TextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
            TextField(value = adress, onValueChange = { adress = it }, label = { Text("Dirección") })
            TextField(value = postalCode, onValueChange = { postalCode = it }, label = { Text("Código Postal") })
            TextField(value = placeOfResidence, onValueChange = { placeOfResidence = it }, label = { Text("Lugar de residencia") })
            TextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Teléfono") })
            TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
            TextField(value = adminEmail, onValueChange = { adminEmail = it }, label = { Text("Email Administrativo") })
            TextField(value = paymentMethodId, onValueChange = { paymentMethodId = it }, label = { Text("ID método de pago") })

            Spacer(Modifier.height(8.dp))

            Button(onClick = {
                if (
                    cif.toIntOrNull() != null &&
                    postalCode.toIntOrNull() != null &&
                    phoneNumber.toIntOrNull() != null &&
                    paymentMethodId.toIntOrNull() != null
                ) {
                    val dto = CustomerDTO(
                        cif = cif.toInt(),
                        name = name,
                        adress = adress,
                        postalCode = postalCode.toInt(),
                        placeOfResidence = placeOfResidence,
                        phoneNumber = phoneNumber.toInt(),
                        email = email,
                        adminEmail = adminEmail,
                        paymentMethodId = paymentMethodId.toInt()
                    )
                    viewModel.createCustomer(dto)
                    cif = ""; name = ""; adress = ""; postalCode = ""
                    placeOfResidence = ""; phoneNumber = ""; email = ""; adminEmail = ""; paymentMethodId = ""
                } else {
                    Toast.makeText(context, "Revisa los campos numéricos", Toast.LENGTH_SHORT).show()
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
        }

        items(customers) { customer ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Nombre: ${customer.name}")
                    Text("Teléfono: ${customer.phoneNumber}")
                    Text("Email: ${customer.email}")
                    Text("Dirección: ${customer.adress}")
                    Text("Cód. Postal: ${customer.postalCode}")
                    Text("CIF: ${customer.cif}")
                    Text("Residencia: ${customer.placeOfResidence}")
                    Text("Email admin: ${customer.adminEmail}")
                    Text("Método de pago ID: ${customer.paymentMethodId}")

                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            viewModel.updateCustomer(
                                customer.id,
                                customer.copy(name = "Actualizado")
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

