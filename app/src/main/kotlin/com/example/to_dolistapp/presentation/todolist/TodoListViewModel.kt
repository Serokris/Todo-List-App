package com.example.to_dolistapp.presentation.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.source.local.PreferencesManager
import com.example.domain.common.SortOrder
import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoInteractor: TodoInteractor,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val preferencesFlow = preferencesManager.preferencesFlow
    val searchQuery = MutableStateFlow("")

    private val _allTodoList = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        todoInteractor.getSortedTodoList(query, filterPreferences.sortOrder)
    }

    val allTodoList = _allTodoList

    fun insert(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoInteractor.insert(todo)
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoInteractor.delete(todo)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            todoInteractor.deleteAll()
        }
    }

    fun deleteAllCompleted() {
        viewModelScope.launch(Dispatchers.IO) {
            todoInteractor.deleteAllCompleted()
        }
    }

    fun getAllCompleted(): Flow<List<Todo>> {
        return todoInteractor.getAllCompleted()
    }

    fun getAllUncompleted(): Flow<List<Todo>> {
        return todoInteractor.getAllUncompleted()
    }

    fun onTodoCheckedChanged(todo: Todo, isChecked: Boolean) {
        viewModelScope.launch {
            todoInteractor.update(todo.copy(isCompleted = isChecked))
        }
    }

    fun onSortedOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }
}