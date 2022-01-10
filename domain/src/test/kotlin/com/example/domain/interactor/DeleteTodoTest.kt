package com.example.domain.interactor

import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteTodoTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)
    private val todo = Todo(0, "Description", false)

    @Test
    fun `Should be called method 'delete' at the repository`() = runBlocking {
        todoInteractor.delete(todo)
        verify(fakeTodoRepository).delete(todo)
    }
}