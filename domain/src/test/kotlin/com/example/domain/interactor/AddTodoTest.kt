package com.example.domain.interactor

import com.example.domain.common.InvalidTodoException
import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class AddTodoTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    @Test
    fun `Add todo, should be called method 'add' at the repository`() = runBlocking {
        val todo = Todo(0, "Description", false)
        todoInteractor.add(todo)
        verify(fakeTodoRepository).insert(todo)
    }

    @Test(expected = InvalidTodoException::class)
    fun `Add todo, expecting exception, because description is empty `() = runBlocking {
        todoInteractor.add(Todo(0, "", false, 0))
    }
}