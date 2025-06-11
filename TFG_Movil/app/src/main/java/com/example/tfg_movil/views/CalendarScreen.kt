package com.example.tfg_movil.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tfg_movil.viewmodel.ViewModelAgenda
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(viewModel: ViewModelAgenda) {
    val entradas by viewModel.entradas.collectAsState()
    val currentDate = remember { mutableStateOf(LocalDate.now()) }

    LaunchedEffect(currentDate.value) {
        viewModel.cargarMes(currentDate.value.year, currentDate.value.monthValue)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(Modifier.height(100.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                currentDate.value = currentDate.value.minusMonths(1)
            }) { Text("<") }

            Text(currentDate.value.format(DateTimeFormatter.ofPattern("MMMM yyyy")))

            Button(onClick = {
                currentDate.value = currentDate.value.plusMonths(1)
            }) { Text(">") }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(entradas) { entrada ->
                Text(
                    text = "${entrada.fechaHora} - ${entrada.cliente} (${entrada.service?.nombre})",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}
