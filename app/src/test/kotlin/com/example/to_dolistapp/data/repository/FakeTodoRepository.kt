package com.example.to_dolistapp.data.repository

import com.example.domain.common.SortOrder
import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import com.example.to_dolistapp.utils.takeIf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTodoRepository : TodoRepository {

    private val todoList = mutableListOf<Todo>()

    override suspend fun insert(todo: Todo) {
        todoList.add(todo)
    }

    override suspend fun update(todo: Todo) {
        val updatedTodo = todoList.find { currentTodo -> currentTodo.id == todo.id }
        todoList[todoList.indexOf(updatedTodo)] = todo
    }

    override suspend fun delete(todo: Todo) {
        todoList.remove(todo)
    }

    override suspend fun deleteAll() {
        todoList.removeAll(todoList)
    }

    override suspend fun deleteAllCompleted() {
        todoList.removeAll { todo -> todo.isCompleted }
    }

    override fun getAllCompleted(): Flow<List<Todo>> {
        return flow {
            emit(todoList.dropWhile { todo -> !todo.isCompleted })
        }
    }

    override fun getAllUncompleted(): Flow<List<Todo>> {
        return flow {
            emit(todoList.dropWhile { todo -> todo.isCompleted })
        }
    }

    override fun getSortedTodoList(searchQuery: String, sortOrder: SortOrder): Flow<List<Todo>> {
        return flow {
            when (sortOrder) {
                SortOrder.BY_NAME -> {
                    emit(todoList.sortedBy { todo -> todo.description }.takeIf { todo ->
                        todo.description.lowercase().contains(searchQuery.lowercase())
                    })
                }
                SortOrder.BY_DATE -> {
                    emit(todoList.sortedBy { todo -> todo.timestamp }.takeIf { todo ->
                        todo.description.lowercase().contains(searchQuery.lowercase())
                    })
                }
            }
        }
    }
}