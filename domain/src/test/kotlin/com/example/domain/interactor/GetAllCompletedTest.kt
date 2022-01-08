package com.example.domain.interactor

import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetAllCompletedTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)
    private val allCompletedTodoList = mutableListOf<Todo>()

    @Before
    fun setUp() = runBlocking {
        ('a'..'z').forEachIndexed { index, char ->
            allCompletedTodoList.add(
                Todo(index, char.toString(), true)
            )
        }
    }

    @Test
    fun getAllCompletedTodoList() = runBlocking {
        Mockito.`when`(fakeTodoRepository.getAllCompleted()).thenReturn(
            flow { emit(allCompletedTodoList) }
        )
        val allCompletedTodoList = todoInteractor.getAllCompleted().first()

        assert(allCompletedTodoList.isNotEmpty())

        allCompletedTodoList.forEach { todo ->
            assert(todo.isCompleted)
        }
    }
}