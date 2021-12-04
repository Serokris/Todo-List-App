package com.example.to_dolistapp.presentation.addtodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoViewModel @Inject constructor(
    private val todoInteractor: TodoInteractor
) : ViewModel() {

    fun insert(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            todoInteractor.insert(todo)
        }
    }
}