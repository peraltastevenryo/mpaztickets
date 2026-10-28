package com.example.mpaztickets.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mpaztickets.ui.componentes.EtiquetaEstado

@Composable
fun TicketsScreen(
    viewModel: TicketViewModel,
    rolUsuario: String,
    nombreUsuario: String,
    onIrACrearTicket: () -> Unit,
    onIrADetalle: (Int) -> Unit,
    onCerrarSesion: () -> Unit
) {
    var usarOrdenPrioridad by remember { mutableStateOf(false) }
    var mostrarDialogoLogout by remember { mutableStateOf(false) }

    val rolLimpio = rolUsuario.lowercase().trim()
    val esAdmin = rolLimpio == "administrador" || rolLimpio == "admin"
    val esResponsable = rolLimpio == "responsable"

    val ticketsFlow = when {
        esAdmin -> {
            if (usarOrdenPrioridad) viewModel.obtenerTicketsPorPrioridad()
            else viewModel.todosLosTickets
        }
        esResponsable -> {
            if (usarOrdenPrioridad) viewModel.obtenerTicketsPorResponsableYPrioridad(nombreUsuario)
            else viewModel.obtenerTicketsPorResponsable(nombreUsuario)
        }
        else -> viewModel.obtenerTicketsPorSolicitante(nombreUsuario)
    }

    val listaTickets by ticketsFlow.collectAsState(initial = emptyList())

    if (mostrarDialogoLogout) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoLogout = false },
            title = { Text("Cerrar Sesion") },
            text = { Text("¿Estas seguro de que deseas cerrar sesion?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoLogout = false
                    onCerrarSesion()
                }) {
                    Text("Si, salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoLogout = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (!esResponsable) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tituloPantalla = when {
                    esAdmin -> "Panel de Administracion"
                    esResponsable -> "Mis Casos Asignados"
                    else -> "Problemas registrados"
                }

                Text(
                    text = tituloPantalla,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { mostrarDialogoLogout = true }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (esAdmin || esResponsable) {
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
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay tickets disponibles")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(listaTickets, key = { it.id }) { ticket ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem()
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
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${ticket.id} ${ticket.titulo}",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))

                                    EtiquetaEstado(estado = ticket.estado)
                                    Text(
                                        text = "Estado: ${ticket.estado}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Ubicación: ${ticket.ubicacion}",
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Text(
                                        text = "Prioridad: ${ticket.prioridad}",
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
