package com.example.to_dolistapp.domain.repository

import com.example.to_dolistapp.data.source.local.SortOrder
import com.example.to_dolistapp.domain.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insert(todo: Todo)

    suspend fun update(todo: Todo)

    suspend fun delete(todo: Todo)

    suspend fun deleteAll()

    suspend fun deleteAllCompleted()

    fun getAllCompleted(): Flow<List<Todo>>

    fun getAllUncompleted(): Flow<List<Todo>>

    fun getSortedTodoList(sortOrder: SortOrder): Flow<List<Todo>>

    fun databaseSearch(searchQuery: String): Flow<List<Todo>>
}