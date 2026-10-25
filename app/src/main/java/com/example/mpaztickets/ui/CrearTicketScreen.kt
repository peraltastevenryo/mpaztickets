package com.example.mpaztickets.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mpaztickets.data.Ticket
import java.io.File

@Composable
fun CrearTicketScreen(
    viewModel: TicketViewModel,
    onTicketGuardado: () -> Unit,
    onVolverAtras: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val photoFile = remember {
        File(context.cacheDir, "ticket_foto_${System.currentTimeMillis()}.jpg")
    }
    val tempUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imagenUri = tempUri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(tempUri)
        } else {
            Toast.makeText(context, "Permiso de camara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imagenUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Ingresar Nuevo Ticket", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Titulo") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripcion del problema") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría (ej: electricidad)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicacion (ej: sala 4)") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(tempUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (imagenUri == null) "Tomar Foto" else "Foto Lista ✓")
            }

            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Galería")
            }
        }

        Button(
            onClick = {
                if (titulo.isNotBlank() && descripcion.isNotBlank()) {
                    val nuevoTicket = Ticket(
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = categoria,
                        ubicacion = ubicacion,
                        solicitante = "Juan Perez",
                        fotoProblema = imagenUri?.toString()
                    )

                    viewModel.insertarTicket(nuevoTicket)
                    Toast.makeText(context, "Ticket guardado", Toast.LENGTH_SHORT).show()

                    onTicketGuardado()
                } else {
                    Toast.makeText(context, "Falta titulo o descripcion", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
        ) {
            Text("Guardar Ticket")
        }

        OutlinedButton(
            onClick = { onVolverAtras() },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("Volver Atras")
        }
    }
}
