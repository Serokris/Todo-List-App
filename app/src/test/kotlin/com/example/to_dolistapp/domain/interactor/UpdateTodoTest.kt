package com.example.to_dolistapp.domain.interactor

import com.example.domain.common.InvalidTodoException
import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import com.example.to_dolistapp.data.repository.FakeTodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateTodoTest {

    private val fakeTodoRepository = FakeTodoRepository()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)
    private val todo = Todo(0, "Description", false)

    @Before
    fun setUp() = runBlocking {
        todoInteractor.add(todo)
    }

    @Test
    fun `Update todo, correct case`() = runBlocking {
        todoInteractor.update(todo.copy(description = "New Description"))

        val updatedTodo = todoInteractor.getAllUncompleted().first()[0]
        assert(updatedTodo != todo)
    }

    @Test(expected = InvalidTodoException::class)
    fun `Update todo, expecting exception, because description is empty`() = runBlocking {
        todoInteractor.update(todo.copy(description = ""))
    }
}