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

class GetAllUncompletedTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)
    private val allUncompletedTodoList = mutableListOf<Todo>()

    @Before
    fun setUp() = runBlocking {
        ('a'..'z').forEachIndexed { index, char ->
            allUncompletedTodoList.add(
                Todo(index, char.toString(), false)
            )
        }
    }

    @Test
    fun getAllUncompletedTodoList() = runBlocking {
        Mockito.`when`(fakeTodoRepository.getAllUncompleted()).thenReturn(
            flow { emit(allUncompletedTodoList) }
        )
        val allUncompletedTodoList = todoInteractor.getAllUncompleted().first()

        assert(allUncompletedTodoList.isNotEmpty())

        allUncompletedTodoList.forEach { todo ->
            assert(!todo.isCompleted)
        }
    }
}