package com.example.domain.repository

import com.example.domain.common.SortOrder
import com.example.domain.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    suspend fun insert(todo: Todo)

    suspend fun update(todo: Todo)

    suspend fun delete(todo: Todo)

    suspend fun deleteAll()

    suspend fun deleteAllCompleted()

    fun getAllCompleted(): Flow<List<Todo>>

    fun getAllUncompleted(): Flow<List<Todo>>

    fun getSortedTodoList(searchQuery: String, sortOrder: SortOrder): Flow<List<Todo>>
}