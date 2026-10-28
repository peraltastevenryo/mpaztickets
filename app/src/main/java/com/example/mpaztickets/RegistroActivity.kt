package com.example.mpaztickets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mpaztickets.data.TicketDatabase
import com.example.mpaztickets.data.UsuarioRepository
import com.example.mpaztickets.ui.RegistroScreen
import com.example.mpaztickets.ui.UsuarioViewModel
import com.example.mpaztickets.ui.UsuarioViewModelFactory
import com.example.mpaztickets.ui.theme.MpazticketsTheme

class RegistroActivity : ComponentActivity() {

    private val usuarioViewModel: UsuarioViewModel by viewModels {
        UsuarioViewModelFactory(
            UsuarioRepository(
                TicketDatabase.getDatabase(applicationContext).usuarioDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MpazticketsTheme {
                RegistroScreen(
                    viewModel = usuarioViewModel,
                    onRegistroExitoso = {
                        finish()
                    },
                    onVolverALogin = {
                        finish()
                    }
                )
            }
        }
    }
}
            RegistroScreen(
                viewModel = usuarioViewModel,
                onRegistroExitoso = {
                    finish()
                },
                onVolverALogin = {
                    finish()
                }
            )
        }
    }
}
