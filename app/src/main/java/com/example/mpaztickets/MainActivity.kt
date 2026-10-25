package com.example.mpaztickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mpaztickets.data.TicketDatabase
import com.example.mpaztickets.data.TicketRepository
import com.example.mpaztickets.data.UsuarioRepository
import com.example.mpaztickets.ui.Navegacion
import com.example.mpaztickets.ui.TicketViewModel
import com.example.mpaztickets.ui.TicketViewModelFactory
import com.example.mpaztickets.ui.UsuarioViewModel
import com.example.mpaztickets.ui.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = TicketDatabase.getDatabase(this)

        val ticketRepository = TicketRepository(database.ticketDao())
        val ticketFactory = TicketViewModelFactory(ticketRepository)

        val usuarioRepository = UsuarioRepository(database.usuarioDao())
        val usuarioFactory = UsuarioViewModelFactory(usuarioRepository)

        setContent {
            val ticketViewModel: TicketViewModel = viewModel(factory = ticketFactory)
            val usuarioViewModel: UsuarioViewModel = viewModel(factory = usuarioFactory)

            Navegacion(
                usuarioViewModel = usuarioViewModel,
                ticketViewModel = ticketViewModel
            )
        }
    }
}