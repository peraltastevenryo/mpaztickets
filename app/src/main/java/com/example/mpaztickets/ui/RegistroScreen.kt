package com.example.mpaztickets.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mpaztickets.ui.componentes.CampoSelector
import com.example.mpaztickets.ui.componentes.CampoTexto
import com.example.mpaztickets.validacion.validarContrasena
import com.example.mpaztickets.validacion.validarCorreo
import com.example.mpaztickets.validacion.validarNombre
import com.example.mpaztickets.validacion.validarSeleccion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    viewModel: UsuarioViewModel,
    onRegistroExitoso: () -> Unit,
    onVolverALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var intentoRegistrar by remember { mutableStateOf(false) }

    val rolesDisponibles = listOf("Usuario", "Responsable", "Admin")
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val errorNombre = validarNombre(nombre)
    val errorCorreo = validarCorreo(correo)
    val errorContrasena = validarContrasena(contrasena)
    val errorRol = validarSeleccion(rol, "un rol")

    val formularioValido = errorNombre == null &&
            errorCorreo == null &&
            errorContrasena == null &&
            errorRol == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onVolverALogin) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            CampoTexto(
                valor = nombre,
                onValorChange = { nombre = it },
                etiqueta = "Nombre completo",
                error = errorNombre,
                mostrarError = intentoRegistrar
            )

            CampoTexto(
                valor = correo,
                onValorChange = { correo = it },
                etiqueta = "Correo electronico",
                error = errorCorreo,
                mostrarError = intentoRegistrar
            )

            CampoTexto(
                valor = contrasena,
                onValorChange = { contrasena = it },
                etiqueta = "Contraseña",
                error = errorContrasena,
                mostrarError = intentoRegistrar,
                esContrasena = true
            )

            CampoSelector(
                valor = rol,
                opciones = rolesDisponibles,
                onSeleccion = { rol = it },
                etiqueta = "Selecciona un rol",
                error = errorRol,
                mostrarError = intentoRegistrar
            )

            Button(
                onClick = {
                    intentoRegistrar = true
                    if (formularioValido) {
                        coroutineScope.launch {
                            val exito = viewModel.registrarUsuario(nombre.trim(), correo.trim(), contrasena, rol)
                            if (exito) {
                                Toast.makeText(context, "Cuenta creada", Toast.LENGTH_SHORT).show()
                                onRegistroExitoso()
                            } else {
                                Toast.makeText(context, "El correo ya esta registrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Registrarse")
            }

            TextButton(onClick = onVolverALogin) {
                Text("¿Ya tienes cuenta? Inicia sesion")
            }
        }
    }
}
