package com.example.mpaztickets.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mpaztickets.data.Ticket

@Composable
fun CrearTicketScreen(
    viewModel: TicketViewModel,
    onTicketGuardado: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Ingresar Nuevo Ticket", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción del problema") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría (ej: Electricidad)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación (ej: Sala 4)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                    val nuevoTicket = Ticket(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        ubicacion = ubicacion,
                        solicitante = "Juan Perez"
                    )

                    viewModel.insertarTicket(nuevoTicket)
                    Toast.makeText(context, "Ticket guardado", Toast.LENGTH_SHORT).show()

                    // Al guardar con éxito, regresa automáticamente al feed
                    onTicketGuardado()
                } else {
                    Toast.makeText(context, "Falta titulo o descripcion", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        ) {
            Text("Guardar Ticket")
        }
    }
}