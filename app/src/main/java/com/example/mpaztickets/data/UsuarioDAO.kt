package com.example.mpaztickets.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Insert
    suspend fun insertarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE rol = 'Responsable' OR rol = 'responsable'")
    fun obtenerResponsables(): Flow<List<Usuario>>

}