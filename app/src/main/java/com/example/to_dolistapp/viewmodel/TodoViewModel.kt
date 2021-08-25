package com.example.to_dolistapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.data.PreferencesManager
import com.example.to_dolistapp.data.SortOrder
import com.example.to_dolistapp.data.Todo
import com.example.to_dolistapp.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        repository.getSortedTodoList(filterPreferences.sortOrder)
    }

    val getAllTodo = tasksFlow.asLiveData()

    fun onSortedOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun insert(todo: Todo) = viewModelScope.launch { repository.insert(todo) }

    fun update(todo: Todo) = viewModelScope.launch { repository.update(todo) }

    fun delete(todo: Todo) = viewModelScope.launch { repository.delete(todo) }

    fun deleteAll() = viewModelScope.launch { repository.deleteAll() }

    fun deleteAllCompleted() = viewModelScope.launch { repository.deleteAllCompleted() }

    fun getAllCompleted(): LiveData<List<Todo>> = repository.getAllCompleted()

    fun getAllUncompleted(): LiveData<List<Todo>> = repository.getAllUncompleted()

    fun databaseSearch(searchQuery: String): LiveData<List<Todo>> = repository.databaseSearch(searchQuery)

    fun onTodoCheckedChanged(todo: Todo, isChecked: Boolean) = viewModelScope.launch {
        repository.update(todo.copy(isCompleted = isChecked))
    }
}