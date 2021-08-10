package com.example.to_dolistapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(tableName = "todo-table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var description: String,
    var isCompleted: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(timestamp)
}