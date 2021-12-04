package com.example.to_dolistapp.di

import android.content.Context
import androidx.room.Room
import com.example.data.source.local.TodoDatabase
import com.example.data.source.local.TodoDatabase.Companion.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        TodoDatabase::class.java,
        DB_NAME
    ).build()

    @Provides
    fun provideTodoDao(db: TodoDatabase) = db.todoDao()
}