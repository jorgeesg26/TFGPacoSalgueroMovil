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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(viewModel: ViewModelAgenda) {
    val entradas by viewModel.entradas.collectAsState()
    val currentDate = remember { mutableStateOf(LocalDate.now()) }

    val month = currentDate.value.month
    val year = currentDate.value.year
    val firstDayOfMonth = LocalDate.of(year, month, 1)
    val daysInMonth = month.length(firstDayOfMonth.isLeapYear)
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Lunes = 1 -> 1 % 7 = 1 (L a D = 0-6)

    val daysList = List(startDayOfWeek) { null } + (1..daysInMonth).map { it }

    LaunchedEffect(currentDate.value) {
        viewModel.cargarMes(year, month.value)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(Modifier.height(100.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                currentDate.value = currentDate.value.minusMonths(1)
            }) { Text("<") }

            Text(
                currentDate.value.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = {
                currentDate.value = currentDate.value.plusMonths(1)
            }) { Text(">") }
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach {
                Text(it, modifier = Modifier.weight(1f))
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysList.size) { index ->
                val day = daysList[index]
                val fecha = day?.let { LocalDate.of(year, month, it) }
                val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
                val eventosDelDia = fecha?.let { f ->
                    entradas.filter {
                        LocalDateTime.parse(it.fechaHora.toString(), formatter).toLocalDate() == f
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .height(64.dp)
                ) {
                    if (day != null) {
                        Text(day.toString())
                        eventosDelDia?.forEach {
                            Text(
                                it.service?.nombre ?: "Evento",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

