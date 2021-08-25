package com.example.to_dolistapp.di

import android.content.Context
import androidx.room.Room
import com.example.to_dolistapp.data.TodoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        TodoDatabase::class.java,
        "todo-list-database"
    ).build()

    @Singleton
    @Provides
    fun provideTodoDao(db: TodoDatabase) = db.todoDao()
}