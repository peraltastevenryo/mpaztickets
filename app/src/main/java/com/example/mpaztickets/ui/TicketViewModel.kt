package com.example.mpaztickets.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mpaztickets.data.Ticket
import com.example.mpaztickets.data.TicketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TicketViewModel(private val repository: TicketRepository) : ViewModel() {

    val todosLosTickets: Flow<List<Ticket>> = repository.todosLosTickets

    fun insertarTicket(ticket: Ticket) {
        viewModelScope.launch {
            repository.insertarTicket(ticket)
        }
    }

    fun actualizarTicket(ticket: Ticket) {
        viewModelScope.launch {
            repository.actualizarTicket(ticket)
        }
    }

    fun asignarResponsableATicket(ticket: Ticket, nuevoResponsable: String) {
        viewModelScope.launch {
            val ticketActualizado = ticket.copy(
                responsable = nuevoResponsable,
                estado = "Asignado"
            )
            repository.actualizarTicket(ticketActualizado)
        }
    }

    suspend fun obtenerTicketPorId(id: Int): Ticket? {
        return repository.obtenerTicketPorId(id)
    }

    fun obtenerTicketsPorSolicitante(nombre: String): Flow<List<Ticket>> {
        return repository.obtenerTicketsPorSolicitante(nombre)
    }

    fun obtenerTicketsPorResponsable(nombre: String): Flow<List<Ticket>> {
        return repository.obtenerTicketsPorResponsable(nombre)
    }

    fun obtenerTicketsPorPrioridad(): Flow<List<Ticket>> {
        return repository.obtenerTicketsPorPrioridad()
    }

    fun obtenerTicketsPorResponsableYPrioridad(nombre: String): Flow<List<Ticket>> {
        return repository.obtenerTicketsPorResponsableYPrioridad(nombre)
    }
}

class TicketViewModelFactory(private val repository: TicketRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicketViewModel::class.java)) {
            return TicketViewModel(repository) as T
        }
        throw IllegalArgumentException("El ViewModel no fue encontrado")
    }
}