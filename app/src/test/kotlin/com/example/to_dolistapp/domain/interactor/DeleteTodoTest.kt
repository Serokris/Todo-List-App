package com.example.to_dolistapp.domain.interactor

import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import com.example.to_dolistapp.data.repository.FakeTodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteTodoTest {

    private val fakeTodoRepository = FakeTodoRepository()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)
    private val todo = Todo(0, "Description", false)

    @Before
    fun setUp() = runBlocking {
        todoInteractor.add(todo)
    }

    @Test
    fun deleteTodo() = runBlocking {
        todoInteractor.delete(todo)

        assert(todoInteractor.getAllUncompleted().first().isEmpty())
    }
}