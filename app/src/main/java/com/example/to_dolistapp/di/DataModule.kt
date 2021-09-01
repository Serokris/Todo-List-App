package com.example.to_dolistapp.di

import com.example.to_dolistapp.data.repository.TodoRepositoryImpl
import com.example.to_dolistapp.data.source.local.TodoDao
import com.example.to_dolistapp.domain.repository.TodoRepository
import com.example.to_dolistapp.domain.usecases.TodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepositoryImpl(todoDao)
    }

    @Singleton
    @Provides
    fun provideTodoUseCase(todoRepository: TodoRepository): TodoUseCase {
        return TodoUseCase(todoRepository)
    }
}