package com.example.mpaztickets

import android.app.ActivityOptions
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
import com.example.mpaztickets.ui.theme.MpazticketsTheme
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
            MpazticketsTheme {
                TicketsScreen(
                    viewModel = ticketViewModel,
                    rolUsuario = rolUsuario,
                    nombreUsuario = nombreUsuario,
                    onIrACrearTicket = {
                        val intent = Intent(this, CrearTicketActivity::class.java).apply {
                            putExtra("NOMBRE_USUARIO", nombreUsuario)
                        }
                        val opciones = ActivityOptions.makeCustomAnimation(
                            this,
                            android.R.anim.slide_in_left,
                            android.R.anim.fade_out
                        )
                        startActivity(intent, opciones.toBundle())
                    },
                    onIrADetalle = { ticketId ->
                        val intent = Intent(this, DetalleTicketActivity::class.java).apply {
                            putExtra("TICKET_ID", ticketId)
                            putExtra("ROL_USUARIO", rolUsuario)
                        }
                        val opciones = ActivityOptions.makeCustomAnimation(
                            this,
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )
                        startActivity(intent, opciones.toBundle())
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
}
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
