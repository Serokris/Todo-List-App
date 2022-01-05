package com.example.to_dolistapp.domain.interactor

import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import com.example.to_dolistapp.data.repository.FakeTodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteAllTodoTest {

    private val fakeTodoRepository = FakeTodoRepository()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    @Before
    fun setUp() = runBlocking {
        ('a'..'z').forEachIndexed { index, char ->
            todoInteractor.add(Todo(index, char.toString(), false))
        }
        println(todoInteractor.getAllUncompleted().first())
    }

    @Test
    fun deleteAllTodo() = runBlocking {
        todoInteractor.deleteAll()

        assert(todoInteractor.getAllUncompleted().first().isEmpty())
    }
}