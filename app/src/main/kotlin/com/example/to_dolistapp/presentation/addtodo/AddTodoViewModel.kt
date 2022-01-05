package com.example.to_dolistapp.presentation.addtodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.InvalidTodoException
import com.example.domain.common.TodoAddUpdateEvent
import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoInteractor: TodoInteractor
) : ViewModel() {

    private val _todoInsertionEvent = MutableStateFlow<TodoAddUpdateEvent>(TodoAddUpdateEvent.Empty)
    val todoInsertionEvent = _todoInsertionEvent.asStateFlow()

    fun insert(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoInteractor.add(todo)
                _todoInsertionEvent.value =
                    TodoAddUpdateEvent.Success("The to-do has been successfully added!")
            } catch (e: InvalidTodoException) {
                _todoInsertionEvent.value =
                    TodoAddUpdateEvent.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}