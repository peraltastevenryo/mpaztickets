package com.example.mpaztickets.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mpaztickets.data.Ticket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTicketScreen(
    viewModel: TicketViewModel,
    ticketId: Int,
    rolUsuario: String,
    onVolverAtras: () -> Unit
) {
    var ticket by remember { mutableStateOf<Ticket?>(null) }
    var estadoSeleccionado by remember { mutableStateOf("") }
    var expandedEstado by remember { mutableStateOf(false) }

    val rolLimpio = rolUsuario.lowercase().trim()
    val esResponsable = rolLimpio == "responsable"
    val estadosPosibles = listOf("Pendiente", "En Proceso", "Resuelto")

    LaunchedEffect(ticketId) {
        val encontrado = viewModel.obtenerTicketPorId(ticketId)
        ticket = encontrado
        if (encontrado != null) {
            estadoSeleccionado = encontrado.estado
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Detalle del Ticket", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            if (ticket == null) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                val currentTicket = ticket!!

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = currentTicket.titulo, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Descripcion:", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        Text(text = currentTicket.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Categoria: ${currentTicket.categoria}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Ubicacion: ${currentTicket.ubicacion}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Prioridad: ${currentTicket.prioridad}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Solicitante: ${currentTicket.solicitante}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        if (esResponsable) {
                            Text(text = "Actualizar Estado:", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(4.dp))

                            ExposedDropdownMenuBox(
                                expanded = expandedEstado,
                                onExpandedChange = { expandedEstado = !expandedEstado },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = estadoSeleccionado,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Estado") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                        .fillMaxWidth()
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedEstado,
                                    onDismissRequest = { expandedEstado = false }
                                ) {
                                    estadosPosibles.forEach { opcion ->
                                        DropdownMenuItem(
                                            text = { Text(opcion) },
                                            onClick = {
                                                estadoSeleccionado = opcion
                                                expandedEstado = false
                                                val ticketActualizado = currentTicket.copy(estado = opcion)
                                                viewModel.actualizarTicket(ticketActualizado)
                                                ticket = ticketActualizado
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            Text(text = "Estado actual: ${currentTicket.estado}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
                        }

                        if (!currentTicket.fotoProblema.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Evidencia fotografica:", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(8.dp))

                            val imagePainter = rememberAsyncImagePainter(model = Uri.parse(currentTicket.fotoProblema))
                            Image(
                                painter = imagePainter,
                                contentDescription = "Foto del problema",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onVolverAtras,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver Atras")
            }
        }
    }
}