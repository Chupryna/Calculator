package com.example.calculator.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.reactivex.Completable
import java.util.concurrent.Callable

@Database(entities = [Expression::class], version = 1)
abstract class CalculatorDatabase : RoomDatabase() {

    abstract fun expressionDao(): ExpressionDao

    companion object {
        private var INSTANCE: CalculatorDatabase? = null

        fun getInstance(context: Context): CalculatorDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    CalculatorDatabase::class.java, "calculator")
                    .allowMainThreadQueries()
                    .build()
               // Completable.fromCallable { Callable { } }.subscribe { getDatabase(context) }.isDisposed
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

        private fun getDatabase(context: Context) {
            /*INSTANCE = Room.databaseBuilder(context,
                CalculatorDatabase::class.java, "calculator")
                .build()*/
        }
    }
}