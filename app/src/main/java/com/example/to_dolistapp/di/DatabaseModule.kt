package com.example.to_dolistapp.di

import android.content.Context
import androidx.room.Room
import com.example.to_dolistapp.data.source.local.TodoDatabase
import com.example.to_dolistapp.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        TodoDatabase::class.java,
        DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideTodoDao(db: TodoDatabase) = db.todoDao()
}