package com.example.to_dolistapp.presentation.updatetodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.InvalidTodoException
import com.example.domain.common.TodoAddUpdateEvent
import com.example.domain.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.domain.interactor.TodoInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UpdateTodoViewModel @Inject constructor(
    private val todoInteractor: TodoInteractor
) : ViewModel() {

    private val _todoUpdateEvent = MutableStateFlow<TodoAddUpdateEvent>(TodoAddUpdateEvent.Empty)
    val todoUpdateEvent = _todoUpdateEvent.asStateFlow()

    fun update(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                todoInteractor.update(todo)
                _todoUpdateEvent.value =
                    TodoAddUpdateEvent.Success("The to-do has been successfully updated!")
            } catch (e: InvalidTodoException) {
                _todoUpdateEvent.value =
                    TodoAddUpdateEvent.Error(e.message ?: "An unexpected error occurred")
            }
        }
    }
}