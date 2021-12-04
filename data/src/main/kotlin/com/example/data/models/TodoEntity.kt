package com.example.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todo-table")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val isCompleted: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable