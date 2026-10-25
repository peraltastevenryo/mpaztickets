package com.example.mpaztickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mpaztickets.data.TicketDatabase
import com.example.mpaztickets.data.TicketRepository
import com.example.mpaztickets.data.UsuarioRepository
import com.example.mpaztickets.ui.DetalleTicketScreen
import com.example.mpaztickets.ui.TicketViewModel
import com.example.mpaztickets.ui.TicketViewModelFactory
import com.example.mpaztickets.ui.UsuarioViewModel
import com.example.mpaztickets.ui.UsuarioViewModelFactory

class DetalleTicketActivity : ComponentActivity() {

    private val ticketViewModel: TicketViewModel by viewModels {
        TicketViewModelFactory(
            TicketRepository(
                TicketDatabase.getDatabase(applicationContext).ticketDao()
            )
        )
    }

    private val usuarioViewModel: UsuarioViewModel by viewModels {
        UsuarioViewModelFactory(
            UsuarioRepository(
                TicketDatabase.getDatabase(applicationContext).usuarioDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ticketId = intent.getIntExtra("TICKET_ID", 0)
        val rolUsuario = intent.getStringExtra("ROL_USUARIO") ?: "Usuario"

        setContent {
            DetalleTicketScreen(
                viewModel = ticketViewModel,
                usuarioViewModel = usuarioViewModel,
                ticketId = ticketId,
                rolUsuario = rolUsuario,
                onVolverAtras = {
                    finish()
                }
            )
        }
    }
}