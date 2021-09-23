package com.example.to_dolistapp.presentation.updatetodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolistapp.domain.interactor.TodoInteractor
import com.example.to_dolistapp.domain.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateTodoViewModel @Inject constructor(
    private val todoInteractor: TodoInteractor
) : ViewModel() {
    fun update(todo: Todo) = viewModelScope.launch(Dispatchers.IO) { todoInteractor.update(todo) }
}