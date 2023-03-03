package com.zkrallah.paymentsassistant.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zkrallah.paymentsassistant.GymApp.Companion.ctx
import com.zkrallah.paymentsassistant.local.entities.Payment
import com.zkrallah.paymentsassistant.local.entities.User

@Database(entities = [User::class, Payment::class], version = 2)
abstract class GymDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun paymentDAO(): PaymentDAO

    companion object {
        private var instance: GymDatabase? = null
        private val context = ctx

        @Synchronized
        fun getInstance(): GymDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(context.applicationContext, GymDatabase::class.java,
                    "gym_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!
        }
    }
}