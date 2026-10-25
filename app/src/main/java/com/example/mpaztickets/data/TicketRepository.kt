package com.example.mpaztickets.data

import kotlinx.coroutines.flow.Flow

class TicketRepository(private val ticketDao: TicketDAO) {

    val todosLosTickets: Flow<List<Ticket>> = ticketDao.obtenerTodosLosTickets()

    suspend fun insertarTicket(ticket: Ticket) {
        ticketDao.insertarTicket(ticket)
    }

    suspend fun actualizarTicket(ticket: Ticket) {
        ticketDao.actualizarTicket(ticket)
    }

    suspend fun obtenerTicketPorId(id: Int): Ticket? {
        return ticketDao.obtenerTicketPorId(id)
    }

    fun obtenerTicketsPorSolicitante(nombre: String): Flow<List<Ticket>> {
        return ticketDao.obtenerTicketsPorSoli(nombre)
    }

    fun obtenerTicketsPorResponsable(nombre: String): Flow<List<Ticket>> {
        return ticketDao.obtenerTicketsPorRespo(nombre)
    }

    fun obtenerTicketsPorPrioridad(): Flow<List<Ticket>> {
        return ticketDao.obtenerTicketsPorPrioridad()
    }
}