package com.example.to_dolistapp.repository

import androidx.lifecycle.LiveData
import com.example.to_dolistapp.data.SortOrder
import com.example.to_dolistapp.data.Todo
import com.example.to_dolistapp.data.TodoDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepository @Inject constructor(private val todoDao: TodoDao) {
    suspend fun insert(todo: Todo) = todoDao.insert(todo)

    suspend fun update(todo: Todo) = todoDao.update(todo)

    suspend fun delete(todo: Todo) = todoDao.delete(todo)

    suspend fun deleteAll() = todoDao.deleteAll()

    suspend fun deleteAllCompleted() = todoDao.deleteAllCompleted()

    fun getAllCompleted(): LiveData<List<Todo>> = todoDao.getAllCompleted()

    fun getAllUncompleted(): LiveData<List<Todo>> = todoDao.getAllUncompleted()

    fun getSortedTodoList(sortOrder: SortOrder): Flow<List<Todo>> = todoDao.getSortedTodoList(sortOrder)

    fun databaseSearch(searchQuery: String): LiveData<List<Todo>> = todoDao.databaseSearch(searchQuery)
}