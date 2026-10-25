package com.example.mpaztickets.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile

@Database(entities = [Ticket::class, Usuario::class], version = 2, exportSchema = false)
abstract class TicketDatabase : RoomDatabase() {

    abstract fun ticketDao(): TicketDAO
    abstract fun usuarioDao(): UsuarioDAO

    companion object {
        @Volatile
        private var INSTANCE: TicketDatabase? = null

        fun getDatabase(context: Context): TicketDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TicketDatabase::class.java,
                    "mpaz_tickets_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}