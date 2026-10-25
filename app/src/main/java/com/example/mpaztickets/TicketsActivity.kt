package com.example.mpaztickets

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mpaztickets.data.TicketDatabase
import com.example.mpaztickets.data.TicketRepository
import com.example.mpaztickets.ui.TicketsScreen
import com.example.mpaztickets.ui.TicketViewModel
import com.example.mpaztickets.ui.TicketViewModelFactory
import kotlin.jvm.java

class TicketsActivity : ComponentActivity() {

    private val ticketViewModel: TicketViewModel by viewModels {
        TicketViewModelFactory(
            TicketRepository(
                TicketDatabase.getDatabase(applicationContext).ticketDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rolUsuario = intent.getStringExtra("ROL_USUARIO") ?: "Usuario"
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO") ?: ""

        setContent {
            TicketsScreen(
                viewModel = ticketViewModel,
                rolUsuario = rolUsuario,
                nombreUsuario = nombreUsuario,
                onIrACrearTicket = {
                    val intent = Intent(this, CrearTicketActivity::class.java).apply {
                        putExtra("NOMBRE_USUARIO", nombreUsuario)
                    }
                    startActivity(intent)
                },
                onIrADetalle = { ticketId ->
                    val intent = Intent(this, DetalleTicketActivity::class.java).apply {
                        putExtra("TICKET_ID", ticketId)
                        putExtra("ROL_USUARIO", rolUsuario)
                    }
                    startActivity(intent)
                },
                onCerrarSesion = {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}