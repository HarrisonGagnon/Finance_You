package com.financeyou.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class StatementRepository (private val statementDAO: StatementDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.

    fun allStatementsRecent(): Flow<List<StatementData>> {
        return statementDAO.getAllStatements()
    }

    fun allStatementsAmountAsc(): Flow<List<StatementData>> {
        return statementDAO.getStatementsOrderedByAmountAsc()
    }

    fun allStatementsAmountDesc(): Flow<List<StatementData>> {
        return statementDAO.getStatementsOrderedByAmountDesc()
    }

    @WorkerThread
    suspend fun upsert(statement: StatementData) {
        statementDAO.upsertStatement(statement)
    }

    @WorkerThread
    suspend fun update(statement: StatementData) {
        statementDAO.updateStatement(statement)
    }

    @WorkerThread
    suspend fun delete(statement: StatementData) {
        statementDAO.deleteStatement(statement)
    }
}