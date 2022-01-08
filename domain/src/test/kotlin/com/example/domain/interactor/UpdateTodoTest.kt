package com.example.domain.interactor

import com.example.domain.common.InvalidTodoException
import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UpdateTodoTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)
    private val todo = Todo(0, "Description", false)

    @Test
    fun `Update todo, correct case`() = runBlocking {
        val newDescription = "New Description"
        todoInteractor.update(todo.copy(description = newDescription))
        verify(fakeTodoRepository).update(todo.copy(description = newDescription))
    }

    @Test(expected = InvalidTodoException::class)
    fun `Update todo, expecting exception, because description is empty`() = runBlocking {
        todoInteractor.update(todo.copy(description = ""))
    }
}