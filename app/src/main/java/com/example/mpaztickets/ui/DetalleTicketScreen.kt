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
    usuarioViewModel: UsuarioViewModel,
    ticketId: Int,
    rolUsuario: String,
    onVolverAtras: () -> Unit
) {
    var ticket by remember { mutableStateOf<Ticket?>(null) }
    var estadoSeleccionado by remember { mutableStateOf("") }
    var expandedEstado by remember { mutableStateOf(false) }

    var responsableSeleccionado by remember { mutableStateOf("") }
    var expandedResponsable by remember { mutableStateOf(false) }

    val listaResponsables by usuarioViewModel.listaResponsables.collectAsState(initial = emptyList())

    val rolLimpio = rolUsuario.lowercase().trim()
    val esResponsable = rolLimpio == "responsable"
    val esAdmin = rolLimpio == "administrador" || rolLimpio == "admin"

    val estadosPosibles = listOf("Pendiente", "Revisado", "Asignado", "En Proceso", "Solucionado", "Cerrado")

    LaunchedEffect(ticketId) {
        val encontrado = viewModel.obtenerTicketPorId(ticketId)
        ticket = encontrado
        if (encontrado != null) {
            estadoSeleccionado = encontrado.estado
            responsableSeleccionado = encontrado.responsable ?: ""
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
                        Text(text = "Responsable actual: ${currentTicket.responsable ?: "Sin asignar"}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))

                        if (esAdmin) {
                            Text(text = "Asignar a Responsable:", style = MaterialTheme.typography.titleSmall)
                            Spacer(modifier = Modifier.height(4.dp))

                            ExposedDropdownMenuBox(
                                expanded = expandedResponsable,
                                onExpandedChange = { expandedResponsable = !expandedResponsable },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = if (responsableSeleccionado.isEmpty()) "Seleccionar responsable" else responsableSeleccionado,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Responsable") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedResponsable) },
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                                        .fillMaxWidth()
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedResponsable,
                                    onDismissRequest = { expandedResponsable = false }
                                ) {
                                    if (listaResponsables.isEmpty()) {
                                        DropdownMenuItem(
                                            text = { Text("No hay usuarios responsables creados") },
                                            onClick = { expandedResponsable = false }
                                        )
                                    } else {
                                        listaResponsables.forEach { usuarioResp ->
                                            DropdownMenuItem(
                                                text = { Text(usuarioResp.nombre) },
                                                onClick = {
                                                    responsableSeleccionado = usuarioResp.nombre
                                                    expandedResponsable = false
                                                    viewModel.asignarResponsableATicket(currentTicket, usuarioResp.nombre)
                                                    ticket = currentTicket.copy(responsable = usuarioResp.nombre, estado = "Asignado")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

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
                        } else if (!esAdmin) {
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