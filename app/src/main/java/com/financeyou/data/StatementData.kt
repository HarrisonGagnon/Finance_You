package com.financeyou.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_flow_table")
data class StatementData(@PrimaryKey val name: String, //TODO: Make unique keys
                         @ColumnInfo (name = "is_income") val isIncome: Boolean, //TODO: Make enum
                         @ColumnInfo val amount: Double,
                         @ColumnInfo val frequency: Frequency,
                         @ColumnInfo (name = "interest_rate") val interestRate: Float,
                         @ColumnInfo (name = "compounding_freq") val compoundingFreq: Frequency)
