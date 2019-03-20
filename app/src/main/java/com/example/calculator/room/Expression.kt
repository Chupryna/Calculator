package com.example.calculator.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class Expression(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    var expression: String,
    var result: String,
    var time: Long,

    @Ignore
    var date: String?
) {
    constructor(expression: String, result: String, time: Long):
            this(null, expression, result, time, SimpleDateFormat("dd.MM.yyyy HH.mm", Locale.getDefault()).format(time))
}