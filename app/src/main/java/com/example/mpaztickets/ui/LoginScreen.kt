package com.example.mpaztickets.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mpaztickets.ui.componentes.CampoTexto
import com.example.mpaztickets.validacion.validarCorreo
import com.example.mpaztickets.validacion.validarObligatorio
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel,
    onLoginExitoso: (String, String) -> Unit,
    onIrARegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var intentoIngresar by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val errorCorreo = validarCorreo(correo)
    val errorContrasena = validarObligatorio(contrasena, "La contraseña")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "MPAZ Ticket", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Inicio de sesion", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        CampoTexto(
            valor = correo,
            onValorChange = { correo = it },
            etiqueta = "Correo electronico",
            error = errorCorreo,
            mostrarError = intentoIngresar
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoTexto(
            valor = contrasena,
            onValorChange = { contrasena = it },
            etiqueta = "Contraseña",
            error = errorContrasena,
            mostrarError = intentoIngresar,
            esContrasena = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                intentoIngresar = true
                if (errorCorreo == null && errorContrasena == null) {
                    coroutineScope.launch {
                        val exito = viewModel.iniciarSesion(correo.trim(), contrasena)
                        if (exito) {
                            val usuario = viewModel.usuarioActual.value
                            if (usuario != null) {
                                onLoginExitoso(usuario.rol, usuario.nombre)
                            }
                        } else {
                            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onIrARegistro) {
            Text("¿No tienes cuenta? Crea una aqui")
        }
    }
}
