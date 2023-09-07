package com.financeyou.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StatementData::class], version = 1)
abstract class StatementsDatabase: RoomDatabase() {
    abstract fun moneyDAO(): StatementDAO

    companion object {
        @Volatile
        private var INSTANCE: StatementsDatabase?= null

        fun getDatabase(context: Context): StatementsDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatementsDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}