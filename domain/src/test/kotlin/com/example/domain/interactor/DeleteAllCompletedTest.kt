package com.example.domain.interactor

import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteAllCompletedTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    @Test
    fun deleteAllCompleted() = runBlocking {
        todoInteractor.deleteAllCompleted()
        verify(fakeTodoRepository).deleteAllCompleted()
    }
}