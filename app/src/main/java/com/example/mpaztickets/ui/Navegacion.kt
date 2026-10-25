package com.example.mpaztickets.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion(
    usuarioViewModel: UsuarioViewModel,
    ticketViewModel: TicketViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                viewModel = usuarioViewModel,
                onLoginExitoso = {
                    
                    navController.navigate("feed_tickets") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onIrARegistro = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onRegistroExitoso = {
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                onVolverALogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("feed_tickets") {
            TicketsScreen(
                viewModel = ticketViewModel,
                onIrACrearTicket = {
                    navController.navigate("crear_ticket")
                }
            )
        }

        composable("crear_ticket") {
            CrearTicketScreen(
                viewModel = ticketViewModel,
                onTicketGuardado = {
                    navController.popBackStack()
                }
            )
        }
    }
}
