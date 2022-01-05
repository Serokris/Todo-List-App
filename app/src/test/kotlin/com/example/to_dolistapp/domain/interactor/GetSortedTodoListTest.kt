package com.example.to_dolistapp.domain.interactor

import com.example.domain.common.SortOrder
import com.example.domain.interactor.TodoInteractor
import com.example.domain.models.Todo
import com.example.to_dolistapp.data.repository.FakeTodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetSortedTodoListTest {

    private val fakeTodoRepository = FakeTodoRepository()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    private val todoA = Todo(0, "Todo A", false, 1)
    private val todoB = Todo(1, "Todo B", false, 0)
    private val todoC = Todo(2, "Todo C", false, 2)

    @Before
    fun setUp() = runBlocking {
        todoInteractor.add(todoA)
        todoInteractor.add(todoB)
        todoInteractor.add(todoC)
    }

    @Test
    fun `Expected full todo list sorted by name, because search query is empty`() =
        runBlocking {
            val sortedTodoList = todoInteractor.getSortedTodoList("", SortOrder.BY_NAME).first()

            for (i in 0..sortedTodoList.size - 2) {
                assert(sortedTodoList[i].description < sortedTodoList[i + 1].description && sortedTodoList.size == 3)
            }
        }

    @Test
    fun `Expected todo list of one item "Todo A" sorted by name, because search query is "a"`() =
        runBlocking {
            val sortedTodoList = todoInteractor.getSortedTodoList("a", SortOrder.BY_NAME).first()
            assert(sortedTodoList[0] == todoA)
        }

    @Test
    fun `Expected full todo list sorted by name, because search query is "Todo"`() =
        runBlocking {
            val sortedTodoList = todoInteractor.getSortedTodoList("Todo", SortOrder.BY_NAME).first()

            for (i in 0..sortedTodoList.size - 2) {
                assert(sortedTodoList[i].description < sortedTodoList[i + 1].description && sortedTodoList.size == 3)
            }
        }

    @Test
    fun `Expected full todo list sorted by date, because search query is empty`() = runBlocking {
        val sortedTodoList = todoInteractor.getSortedTodoList("", SortOrder.BY_DATE).first()

        for (i in 0..sortedTodoList.size - 2) {
            assert(sortedTodoList[i].timestamp < sortedTodoList[i + 1].timestamp && sortedTodoList.size == 3)
        }
    }

    @Test
    fun `Expected todo list of one item "Todo B" sorted by date, because search query is "b"`() =
        runBlocking {
            val sortedTodoList = todoInteractor.getSortedTodoList("b", SortOrder.BY_DATE).first()
            assert(sortedTodoList[0] == todoB)
        }

    @Test
    fun `Expected full todo list sorted by date, because search query is "Todo"`() = runBlocking {
        val sortedTodoList = todoInteractor.getSortedTodoList("Todo", SortOrder.BY_DATE).first()

        for (i in 0..sortedTodoList.size - 2) {
            assert(sortedTodoList[i].timestamp < sortedTodoList[i + 1].timestamp && sortedTodoList.size == 3)
        }
    }
}