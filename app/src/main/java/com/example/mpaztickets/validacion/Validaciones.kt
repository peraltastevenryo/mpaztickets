package com.example.mpaztickets.validacion

import android.util.Patterns

fun validarObligatorio(valor: String, nombreCampo: String): String? {
    return if (valor.isBlank()) "$nombreCampo es obligatorio" else null
}

fun validarCorreo(correo: String): String? {
    return when {
        correo.isBlank() -> "El correo es obligatorio"
        !Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches() -> "Formato de correo no válido"
        else -> null
    }
}

fun validarContrasena(contrasena: String): String? {
    return when {
        contrasena.isBlank() -> "La contraseña es obligatoria"
        contrasena.length < 6 -> "Debe tener al menos 6 caracteres"
        else -> null
    }
}

fun validarNombre(nombre: String): String? {
    return when {
        nombre.isBlank() -> "El nombre es obligatorio"
        nombre.trim().length < 3 -> "Debe tener al menos 3 letras"
        nombre.any { it.isDigit() } -> "El nombre no puede tener números"
        else -> null
    }
}

fun validarTitulo(titulo: String): String? {
    return when {
        titulo.isBlank() -> "El título es obligatorio"
        titulo.trim().length < 5 -> "Mínimo 5 caracteres"
        titulo.length > 50 -> "Máximo 50 caracteres"
        else -> null
    }
}

fun validarDescripcion(descripcion: String): String? {
    return when {
        descripcion.isBlank() -> "La descripción es obligatoria"
        descripcion.trim().length < 10 -> "Describe el problema con al menos 10 caracteres"
        else -> null
    }
}

fun validarSeleccion(valor: String, nombreCampo: String): String? {
    return if (valor.isBlank()) "Debes seleccionar $nombreCampo" else null
}
