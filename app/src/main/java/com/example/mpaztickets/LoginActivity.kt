package com.example.mpaztickets

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.mpaztickets.data.TicketDatabase
import com.example.mpaztickets.data.UsuarioRepository
import com.example.mpaztickets.ui.LoginScreen
import com.example.mpaztickets.ui.UsuarioViewModel
import com.example.mpaztickets.ui.UsuarioViewModelFactory
import kotlin.jvm.java

class LoginActivity : ComponentActivity() {

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
            LoginScreen(
                viewModel = usuarioViewModel,
                onLoginExitoso = { rol, nombre ->
                    val intent = Intent(this, TicketsActivity::class.java).apply {
                        putExtra("ROL_USUARIO", rol)
                        putExtra("NOMBRE_USUARIO", nombre)
                    }
                    startActivity(intent)
                    finish()
                },
                onIrARegistro = {
                    val intent = Intent(this, RegistroActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}