package com.example.to_dolistapp.domain.interactor

import com.example.domain.common.InvalidTodoException
import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import com.example.to_dolistapp.data.repository.FakeTodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddTodoTest {

    private val fakeTodoRepository = FakeTodoRepository()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    @Test
    fun `Add todo, correct case`() = runBlocking {
        val todo = Todo(
            0, "Description", false
        )
        todoInteractor.add(todo)

        assert(todoInteractor.getAllUncompleted().first().isNotEmpty())
    }

    @Test(expected = InvalidTodoException::class)
    fun `Add todo, expecting exception, because description is empty `() = runBlocking {
        val todo = Todo(
            0, "", false, 0
        )
        todoInteractor.add(todo)
    }
}