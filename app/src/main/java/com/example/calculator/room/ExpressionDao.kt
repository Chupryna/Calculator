package com.example.calculator.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface ExpressionDao {
    @Query("SELECT * FROM expression")
    fun getAll(): Flowable<List<Expression>>

    @Insert
    fun insert(expression: Expression): Long

    @Delete
    fun delete(expression: Expression): Int

    @Query("DELETE FROM expression")
    fun deleteAll(): Int
}