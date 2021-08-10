package com.example.to_dolistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.data.PreferencesManager
import com.example.to_dolistapp.data.SortOrder
import com.example.to_dolistapp.data.Todo
import com.example.to_dolistapp.data.TodoDatabase
import com.example.to_dolistapp.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TodoViewModel (application: Application) : AndroidViewModel(application) {

    private val preferencesManager: PreferencesManager = PreferencesManager(application)
    private val todoRepository : TodoRepository = TodoRepository(TodoDatabase.getDataBase(application))

    private val searchQuery = MutableStateFlow("")
    private val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        todoRepository.getSortedTodoList(filterPreferences.sortOrder)
    }

    val getAllTodo = tasksFlow.asLiveData()

    fun onSortedOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun insert(todo: Todo) = viewModelScope.launch { todoRepository.insert(todo) }

    fun update(todo: Todo) = viewModelScope.launch { todoRepository.update(todo) }

    fun delete(todo: Todo) = viewModelScope.launch { todoRepository.delete(todo) }

    fun deleteAll() = viewModelScope.launch { todoRepository.deleteAll() }

    fun deleteAllCompleted() = viewModelScope.launch { todoRepository.deleteAllCompleted() }

    fun getAllCompleted(): LiveData<List<Todo>> = todoRepository.getAllCompleted()

    fun getAllUncompleted(): LiveData<List<Todo>> = todoRepository.getAllUncompleted()

    fun databaseSearch(searchQuery: String): LiveData<List<Todo>> = todoRepository.databaseSearch(searchQuery)

    fun onTodoCheckedChanged(todo: Todo, isChecked: Boolean) = viewModelScope.launch {
        todoRepository.update(todo.copy(isCompleted = isChecked))
    }
}