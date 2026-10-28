package com.example.mpaztickets.ui.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EtiquetaEstado(estado: String) {
    val colorObjetivo = when (estado) {
        "Pendiente" -> Color(0xFF9E9E9E)
        "Revisado" -> Color(0xFF9C27B0)
        "Asignado" -> Color(0xFF0288D1)
        "En Proceso" -> Color(0xFFF57C00)
        "Solucionado" -> Color(0xFF43A047)
        "Cerrado" -> Color(0xFFD32F2F)
        else -> Color(0xFFD32F2F)
    }

    val colorAnimado by animateColorAsState(
        targetValue = colorObjetivo,
        animationSpec = tween(600),
        label = "colorEstado"
    )

    Text(
        text = estado,
        color = Color.White,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(colorAnimado)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
