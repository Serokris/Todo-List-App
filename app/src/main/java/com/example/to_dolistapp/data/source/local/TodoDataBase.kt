package com.example.to_dolistapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.to_dolistapp.domain.models.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}