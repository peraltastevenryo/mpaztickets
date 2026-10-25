package com.example.mpaztickets.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDAO {
    @Insert
    suspend fun insertarTicket(ticket: Ticket)

    @Update
    suspend fun actualizarTicket(ticket: Ticket)

    @Query("SELECT * FROM tickets ORDER BY fechaCreacion DESC")
    fun obtenerTodosLosTickets(): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE solicitante = :nombreSolicitante ORDER BY fechaCreacion DESC")
    fun obtenerTicketsPorSoli(nombreSolicitante: String): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE responsable = :nombreResponsable ORDER BY fechaCreacion DESC")
    fun obtenerTicketsPorRespo(nombreResponsable: String): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets ORDER BY CASE prioridad WHEN 'Alta' THEN 1 WHEN 'Normal' THEN 2 WHEN 'Baja' THEN 3 ELSE 4 END")
    fun obtenerTicketsPorPrioridad(): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE id = :ticketId")
    suspend fun obtenerTicketPorId(ticketId: Int): Ticket?

    @Query("SELECT * FROM tickets WHERE responsable = :nombreResponsable ORDER BY CASE prioridad WHEN 'Alta' THEN 1 WHEN 'Normal' THEN 2 WHEN 'Baja' THEN 3 ELSE 4 END")
    fun obtenerTicketsPorResponsableYPrioridad(nombreResponsable: String): Flow<List<Ticket>>

}