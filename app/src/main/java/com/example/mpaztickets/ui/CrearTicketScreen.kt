package com.example.mpaztickets.ui

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.mpaztickets.data.Ticket
import com.example.mpaztickets.data.listaCategorias
import com.example.mpaztickets.data.listaPrioridades
import com.example.mpaztickets.data.listaUbicaciones
import com.example.mpaztickets.ui.componentes.CampoSelector
import com.example.mpaztickets.ui.componentes.CampoTexto
import com.example.mpaztickets.validacion.validarDescripcion
import com.example.mpaztickets.validacion.validarSeleccion
import com.example.mpaztickets.validacion.validarTitulo
import java.io.File

@Composable
fun CrearTicketScreen(
    viewModel: TicketViewModel,
    nombreUsuario: String,
    onTicketGuardado: () -> Unit,
    onVolverAtras: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("Normal") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var intentoGuardar by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val errorTitulo = validarTitulo(titulo)
    val errorDescripcion = validarDescripcion(descripcion)
    val errorCategoria = validarSeleccion(categoria, "una categoria")
    val errorUbicacion = validarSeleccion(ubicacion, "una ubicacion")

    val formularioValido = errorTitulo == null &&
            errorDescripcion == null &&
            errorCategoria == null &&
            errorUbicacion == null

    val photoFile = remember {
        File(context.cacheDir, "ticket_foto_${System.currentTimeMillis()}.jpg")
    }
    val tempUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) imagenUri = tempUri
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
        if (uri != null) imagenUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Ingresar Nuevo Ticket", style = MaterialTheme.typography.titleLarge)

        CampoTexto(
            valor = titulo,
            onValorChange = { titulo = it },
            etiqueta = "Titulo",
            error = errorTitulo,
            mostrarError = intentoGuardar
        )

        CampoTexto(
            valor = descripcion,
            onValorChange = { descripcion = it },
            etiqueta = "Descripcion del problema",
            error = errorDescripcion,
            mostrarError = intentoGuardar
        )

        CampoSelector(
            valor = categoria,
            opciones = listaCategorias,
            onSeleccion = { categoria = it },
            etiqueta = "Categoria",
            error = errorCategoria,
            mostrarError = intentoGuardar
        )

        CampoSelector(
            valor = ubicacion,
            opciones = listaUbicaciones,
            onSeleccion = { ubicacion = it },
            etiqueta = "Ubicacion",
            error = errorUbicacion,
            mostrarError = intentoGuardar
        )

        CampoSelector(
            valor = prioridad,
            opciones = listaPrioridades,
            onSeleccion = { prioridad = it },
            etiqueta = "Prioridad",
            error = null,
            mostrarError = false
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val permiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    if (permiso == PackageManager.PERMISSION_GRANTED) {
                        cameraLauncher.launch(tempUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Tomar Foto")
            }

            OutlinedButton(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Galeria")
            }
        }

        AnimatedVisibility(visible = imagenUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imagenUri),
                contentDescription = "Vista previa de la foto",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Button(
            onClick = {
                intentoGuardar = true
                if (formularioValido) {
                    val nuevoTicket = Ticket(
                        titulo = titulo.trim(),
                        descripcion = descripcion.trim(),
                        categoria = categoria,
                        ubicacion = ubicacion,
                        prioridad = prioridad,
                        solicitante = nombreUsuario,
                        fotoProblema = imagenUri?.toString()
                    )
                    viewModel.insertarTicket(nuevoTicket)
                    Toast.makeText(context, "Ticket guardado", Toast.LENGTH_SHORT).show()
                    onTicketGuardado()
                } else {
                    Toast.makeText(context, "Revisa los campos marcados en rojo", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Guardar Ticket")
        }

        OutlinedButton(
            onClick = { onVolverAtras() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver Atras")
        }
    }
}
