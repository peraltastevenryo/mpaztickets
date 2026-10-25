package com.example.mpaztickets.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mpaztickets.data.Usuario
import com.example.mpaztickets.data.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual

    val listaResponsables: Flow<List<Usuario>> = repository.obtenerResponsables()

    suspend fun registrarUsuario(nombre: String, correo: String, contrasena: String, rol: String): Boolean {
        val existe = repository.buscarPorCorreo(correo)
        if (existe != null) {
            return false
        }

        val nuevoUsuario = Usuario(nombre = nombre, correo = correo, contrasena = contrasena, rol = rol)
        repository.insertarUsuario(nuevoUsuario)

        return true
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = repository.iniciarSesion(correo, contrasena)
        if (usuario != null) {
            _usuarioActual.value = usuario
            return true
        }

        return false
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
    }
}

class UsuarioViewModelFactory(private val repository: UsuarioRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel no encontrado")
    }
}