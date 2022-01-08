package com.example.domain.interactor

import com.example.domain.common.InvalidTodoException
import com.example.domain.common.SortOrder
import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoInteractor @Inject constructor(private val todoRepository: TodoRepository) {

    suspend fun add(todo: Todo) {
        if (todo.description.isBlank()) {
            throw InvalidTodoException("The todo description should not be empty!")
        }
        todoRepository.insert(todo)
    }

    suspend fun update(todo: Todo) {
        if (todo.description.isBlank()) {
            throw InvalidTodoException("The todo description should not be empty!")
        }
        todoRepository.update(todo)
    }

    suspend fun delete(todo: Todo) = todoRepository.delete(todo)

    suspend fun deleteAll() = todoRepository.deleteAll()

    suspend fun deleteAllCompleted() = todoRepository.deleteAllCompleted()

    fun getAllCompleted(): Flow<List<Todo>> = todoRepository.getAllCompleted()

    fun getAllUncompleted(): Flow<List<Todo>> = todoRepository.getAllUncompleted()

    fun getSortedTodoList(searchQuery: String, sortOrder: SortOrder): Flow<List<Todo>> {
        return todoRepository.getSortedTodoList(searchQuery, sortOrder)
    }
}