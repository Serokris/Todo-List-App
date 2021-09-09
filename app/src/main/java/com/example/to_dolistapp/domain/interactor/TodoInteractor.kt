package com.example.to_dolistapp.domain.interactor

import com.example.to_dolistapp.data.source.local.SortOrder
import com.example.to_dolistapp.domain.models.Todo
import com.example.to_dolistapp.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoInteractor @Inject constructor(private val todoRepository: TodoRepository) {
    suspend fun insert(todo: Todo) = todoRepository.insert(todo)

    suspend fun update(todo: Todo) = todoRepository.update(todo)

    suspend fun delete(todo: Todo) = todoRepository.delete(todo)

    suspend fun deleteAll() = todoRepository.deleteAll()

    suspend fun deleteAllCompleted() = todoRepository.deleteAllCompleted()

    fun getAllCompleted(): Flow<List<Todo>> = todoRepository.getAllCompleted()

    fun getAllUncompleted(): Flow<List<Todo>> = todoRepository.getAllUncompleted()

    fun getSortedTodoList(sortOrder: SortOrder): Flow<List<Todo>> {
        return todoRepository.getSortedTodoList(sortOrder)
    }

    fun databaseSearch(searchQuery: String): Flow<List<Todo>> {
        return todoRepository.databaseSearch(searchQuery)
    }
}