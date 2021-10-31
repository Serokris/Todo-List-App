package com.example.domain.models

import java.text.DateFormat

data class Todo(
    val id: Int,
    var description: String,
    var isCompleted: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    val createdDateFormatted: String = DateFormat.getDateTimeInstance().format(timestamp)
}