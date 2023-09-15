package com.financeyou.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StatementDAO {
    /**
     * @brief Insert StatementData into Database
     */
    @Upsert
    suspend fun upsertStatement(statementData: StatementData)

    /**
     * @brief Updates StatementData in the database with a matching primary key
     */
    @Update
    suspend fun updateStatement(statementData: StatementData)

    /**
     * @brief Delete StatementData from Database
     */
    @Delete
    suspend fun deleteStatement(statementData: StatementData)

    /**
     * @brief Gets all StatementData from the database
     */
    @Query("SELECT * FROM statement_table")
    fun getAllStatements(): Flow<List<StatementData>>

    /**
     * @brief Get StatementData from Database ordered by descending amount
     */
    @Query("SELECT * FROM statement_table ORDER BY amount DESC")
    fun getStatementsOrderedByAmountDesc(): Flow<List<StatementData>>

    /**
     * @brief Get StatementData from Database ordered by ascending amount
     */
    @Query("SELECT * FROM statement_table ORDER BY amount ASC")
    fun getStatementsOrderedByAmountAsc(): Flow<List<StatementData>>
}