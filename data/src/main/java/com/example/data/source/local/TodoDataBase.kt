package com.example.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.models.TodoEntity

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}