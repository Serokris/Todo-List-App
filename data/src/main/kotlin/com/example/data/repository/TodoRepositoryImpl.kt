package com.example.data.repository

import com.example.data.mappers.toTodo
import com.example.data.mappers.toTodoEntity
import com.example.data.source.local.TodoDao
import com.example.domain.common.SortOrder
import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao
) : TodoRepository {

    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo.toTodoEntity())
    }

    override suspend fun update(todo: Todo) {
        todoDao.update(todo.toTodoEntity())
    }

    override suspend fun delete(todo: Todo) {
        todoDao.delete(todo.toTodoEntity())
    }

    override suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    override suspend fun deleteAllCompleted() {
        todoDao.deleteAllCompleted()
    }

    override fun getAllCompleted(): Flow<List<Todo>> {
        return todoDao.getAllCompleted()
            .map { todoEntityList -> todoEntityList.map { todoEntity -> todoEntity.toTodo() } }
    }

    override fun getAllUncompleted(): Flow<List<Todo>> {
        return todoDao.getAllUncompleted()
            .map { todoEntityList -> todoEntityList.map { todoEntity -> todoEntity.toTodo() } }
    }

    override fun getSortedTodoList(sortOrder: SortOrder): Flow<List<Todo>> {
        return todoDao.getSortedTodoList(sortOrder)
            .map { todoEntityList -> todoEntityList.map { todoEntity -> todoEntity.toTodo() } }
    }

    override fun databaseSearch(searchQuery: String): Flow<List<Todo>> {
        return todoDao.databaseSearch(searchQuery)
            .map { todoEntityList -> todoEntityList.map { todoEntity -> todoEntity.toTodo() } }
    }
}