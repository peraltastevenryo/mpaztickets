package com.example.mpaztickets.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TicketsScreen(
    viewModel: TicketViewModel,
    rolUsuario: String,
    nombreUsuario: String,
    onIrACrearTicket: () -> Unit,
    onIrADetalle: (Int) -> Unit
) {
    var usarOrdenPrioridad by remember { mutableStateOf(false) }

    val rolLimpio = rolUsuario.lowercase().trim()

    val ticketsFlow = when (rolLimpio) {
        "administrador", "admin" -> {
            if (usarOrdenPrioridad) viewModel.obtenerTicketsPorPrioridad()
            else viewModel.todosLosTickets
        }
        "responsable" -> viewModel.obtenerTicketsPorResponsable(nombreUsuario)
        else -> viewModel.obtenerTicketsPorSolicitante(nombreUsuario)
    }

    val listaTickets by ticketsFlow.collectAsState(initial = emptyList())

    val esAdmin = rolLimpio == "administrador" || rolLimpio == "admin"

    Scaffold(
        floatingActionButton = {
            if (rolLimpio != "responsable") {
                FloatingActionButton(onClick = onIrACrearTicket) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear nuevo ticket"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            val tituloPantalla = when (rolLimpio) {
                "administrador", "admin" -> "Panel de Administración"
                "responsable" -> "Mis Casos Asignados"
                else -> "Problemas registrados"
            }

            Text(text = tituloPantalla, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))

            if (esAdmin) {
                Button(
                    onClick = { usarOrdenPrioridad = !usarOrdenPrioridad },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (usarOrdenPrioridad) "Mostrando por Prioridad (Cambiar)" else "Ordenar por Prioridad")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (listaTickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(text = "No hay tickets disponibles.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(listaTickets) { ticket ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onIrADetalle(ticket.id) },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = ticket.titulo,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Prioridad: ${ticket.prioridad}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = ticket.descripcion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Categoria: ${ticket.categoria}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "Ubicacion: ${ticket.ubicacion}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}