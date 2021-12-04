package com.example.to_dolistapp.di

import com.example.data.repository.TodoRepositoryImpl
import com.example.data.source.local.TodoDao
import com.example.domain.interactor.TodoInteractor
import com.example.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepositoryImpl(todoDao)
    }

    @Provides
    @Singleton
    fun provideTodoInteractor(todoRepository: TodoRepository): TodoInteractor {
        return TodoInteractor(todoRepository)
    }
}