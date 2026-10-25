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
                onLoginExitoso = { rol, nombre ->
                    navController.navigate("feed_tickets/$rol/$nombre") {
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

        composable("feed_tickets/{rolUsuario}/{nombreUsuario}") { backStackEntry ->
            val rolUsuario = backStackEntry.arguments?.getString("rolUsuario") ?: "Usuario"
            val nombreUsuario = backStackEntry.arguments?.getString("nombreUsuario") ?: ""

            TicketsScreen(
                viewModel = ticketViewModel,
                rolUsuario = rolUsuario,
                nombreUsuario = nombreUsuario,
                onIrACrearTicket = {
                    navController.navigate("crear_ticket/$nombreUsuario")
                },
                onIrADetalle = { ticketId ->
                    navController.navigate("detalle_ticket/$ticketId/$rolUsuario")
                }
            )
        }

        composable("crear_ticket/{nombreUsuario}") { backStackEntry ->
            val nombreUsuario = backStackEntry.arguments?.getString("nombreUsuario") ?: ""

            CrearTicketScreen(
                viewModel = ticketViewModel,
                nombreUsuario = nombreUsuario,
                onTicketGuardado = {
                    navController.popBackStack()
                },
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }

        composable("detalle_ticket/{ticketId}/{rolUsuario}") { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId")?.toIntOrNull() ?: 0
            val rolUsuario = backStackEntry.arguments?.getString("rolUsuario") ?: "Usuario"

            DetalleTicketScreen(
                viewModel = ticketViewModel,
                ticketId = ticketId,
                rolUsuario = rolUsuario,
                onVolverAtras = {
                    navController.popBackStack()
                }
            )
        }
    }
}