package com.example.to_dolistapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.data.source.local.PreferencesManager
import com.example.to_dolistapp.data.source.local.SortOrder
import com.example.to_dolistapp.domain.models.Todo
import com.example.to_dolistapp.domain.usecases.TodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoUseCase: TodoUseCase,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val searchQuery = MutableStateFlow("")
    private val preferencesFlow = preferencesManager.preferencesFlow

    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (_, filterPreferences) ->
        todoUseCase.getSortedTodoList(filterPreferences.sortOrder)
    }

    val getAllTodo = tasksFlow.asLiveData()

    fun onSortedOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun insert(todo: Todo) = viewModelScope.launch(Dispatchers.IO) { todoUseCase.insert(todo) }

    fun update(todo: Todo) = viewModelScope.launch(Dispatchers.IO) { todoUseCase.update(todo) }

    fun delete(todo: Todo) = viewModelScope.launch(Dispatchers.IO) { todoUseCase.delete(todo) }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) { todoUseCase.deleteAll() }

    fun deleteAllCompleted() = viewModelScope.launch(Dispatchers.IO) { todoUseCase.deleteAllCompleted() }

    fun getAllCompleted(): LiveData<List<Todo>> = todoUseCase.getAllCompleted().asLiveData()

    fun getAllUncompleted(): LiveData<List<Todo>> {
        return todoUseCase.getAllUncompleted().asLiveData()
    }

    fun databaseSearch(searchQuery: String): LiveData<List<Todo>> {
        return todoUseCase.databaseSearch(searchQuery).asLiveData()
    }

    fun onTodoCheckedChanged(todo: Todo, isChecked: Boolean) = viewModelScope.launch {
        todoUseCase.update(todo.copy(isCompleted = isChecked))
    }
}