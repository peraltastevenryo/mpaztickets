package com.example.mpaztickets.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    mostrarError: Boolean,
    esContrasena: Boolean = false
) {
    val hayError = mostrarError && error != null

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValorChange,
            label = { Text(etiqueta) },
            isError = hayError,
            singleLine = true,
            visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (hayError) {
                    Icon(Icons.Filled.Error, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                } else if (mostrarError) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "Correcto", tint = MaterialTheme.colorScheme.primary)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        MensajeError(visible = hayError, texto = error ?: "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoSelector(
    valor: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit,
    etiqueta: String,
    error: String?,
    mostrarError: Boolean
) {
    var expandido by remember { mutableStateOf(false) }
    val hayError = mostrarError && error != null

    Column(modifier = Modifier.fillMaxWidth()) {
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                readOnly = true,
                label = { Text(etiqueta) },
                isError = hayError,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSeleccion(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }

        MensajeError(visible = hayError, texto = error ?: "")
    }
}

@Composable
fun MensajeError(visible: Boolean, texto: String) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = texto,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
