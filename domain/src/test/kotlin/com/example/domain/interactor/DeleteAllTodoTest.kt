package com.example.domain.interactor

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteAllTodoTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    @Test
    fun `Should be called method 'deleteAll' at the repository`() = runBlocking {
        todoInteractor.deleteAll()
        verify(fakeTodoRepository).deleteAll()
    }
}