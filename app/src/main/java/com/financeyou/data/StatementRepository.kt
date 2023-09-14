//package com.financeyou.data
//
//import androidx.annotation.WorkerThread
//import androidx.lifecycle.LiveData
//
//class StatementRepository (private val statementDAO: StatementDAO) {
//
//    // Room executes all queries on a separate thread.
//    // Observed Flow will notify the observer when the data has changed.
//    val allStatements: LiveData<List<StatementData>> = statementDAO.getAllStatements()
//
//    // By default Room runs suspend queries off the main thread, therefore, we don't need to
//    // implement anything else to ensure we're not doing long running database work
//    // off the main thread.
//    @WorkerThread
//    fun insert(statement: StatementData) {
//        statementDAO.upsertStatement(statement)
//    }
//
//    @WorkerThread
//    fun delete(statementName: String) {
//        statementDAO.deleteStatement(statementName)
//    }
//}