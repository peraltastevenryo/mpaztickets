package com.example.mpaztickets.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class Ticket (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val ubicacion: String,
    val estado: String = "Pendiente",
    val prioridad: String = "Normal",
    val solicitante: String,
    val responsable: String? = null,
    val comentarios: String = "",
    val fechaCreacion: Long = System.currentTimeMillis(),
    val fotoProblema: String? = null
)