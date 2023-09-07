package com.financeyou.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StatementDAO {
    /**
     * @brief Gets all MoneyData from the database
     */
    @Query("SELECT * FROM cash_flow_table")
    fun getAll(): LiveData<List<StatementData>>

    /**
     * @brief Insert Money Data into Database
     * @note THIS DOES NOT NECESSARILY MEAN INCOME!
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMoney(statementData: StatementData)

    /**
     * @brief Delete Cash Flow Data from Database
     * @note THIS DOES NOT NECESSARILY MEAN EXPENSES!
     */
    @Query ("DELETE FROM cash_flow_table WHERE name = :moneyDataName")
    fun deleteMoney(moneyDataName: String)
}