package com.example.mpaztickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mpaztickets.data.TicketDatabase
import com.example.mpaztickets.data.TicketRepository
import com.example.mpaztickets.ui.CrearTicketScreen
import com.example.mpaztickets.ui.TicketViewModel
import com.example.mpaztickets.ui.TicketViewModelFactory
import com.example.mpaztickets.ui.theme.MpazticketsTheme

class CrearTicketActivity : ComponentActivity() {

    private val ticketViewModel: TicketViewModel by viewModels {
        TicketViewModelFactory(
            TicketRepository(
                TicketDatabase.getDatabase(applicationContext).ticketDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO") ?: ""

        setContent {
            MpazticketsTheme {
                CrearTicketScreen(
                    viewModel = ticketViewModel,
                    nombreUsuario = nombreUsuario,
                    onTicketGuardado = {
                        finish()
                    },
                    onVolverAtras = {
                        finish()
                    }
                )
            }
        }
    }
}
