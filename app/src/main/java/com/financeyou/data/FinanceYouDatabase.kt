package com.financeyou.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StatementData::class],
    version = 1
)
abstract class FinanceYouDatabase: RoomDatabase() {
    abstract val statementDAO: StatementDAO
}