package com.example.to_dolistapp.data.repository

import com.example.to_dolistapp.data.source.local.SortOrder
import com.example.to_dolistapp.data.source.local.TodoDao
import com.example.to_dolistapp.domain.models.Todo
import com.example.to_dolistapp.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val todoDao: TodoDao) : TodoRepository {
    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo)
    }

    override suspend fun update(todo: Todo) {
        todoDao.update(todo)
    }

    override suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    override suspend fun deleteAll() {
        todoDao.deleteAll()
    }

    override suspend fun deleteAllCompleted() {
        todoDao.deleteAllCompleted()
    }

    override fun getAllCompleted(): Flow<List<Todo>> {
        return todoDao.getAllCompleted()
    }

    override fun getAllUncompleted(): Flow<List<Todo>> {
        return todoDao.getAllUncompleted()
    }

    override fun getSortedTodoList(sortOrder: SortOrder): Flow<List<Todo>> {
        return todoDao.getSortedTodoList(sortOrder)
    }

    override fun databaseSearch(searchQuery: String): Flow<List<Todo>> {
        return todoDao.databaseSearch(searchQuery)
    }
}