package com.example.mpaztickets.data

import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDAO) {

    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        return usuarioDao.iniciarSesion(correo, contrasena)
    }

    suspend fun buscarPorCorreo(correo: String): Usuario? {
        return usuarioDao.buscarPorCorreo(correo)
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizarUsuario(usuario)
    }

    fun obtenerResponsables(): Flow<List<Usuario>> {
        return usuarioDao.obtenerResponsables()
    }

}