package com.example.domain.interactor

import com.example.domain.common.SortOrder
import com.example.domain.common.takeIf
import com.example.domain.models.Todo
import com.example.domain.repository.TodoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetSortedTodoListTest {

    private val fakeTodoRepository = mock<TodoRepository>()
    private val todoInteractor = TodoInteractor(fakeTodoRepository)

    private val todoA = Todo(1, "Todo A", false, 1)
    private val todoB = Todo(0, "Todo B", false, 0)
    private val todoC = Todo(2, "Todo C", false, 2)
    private val todoList = listOf(todoB, todoA, todoC)

    @Test
    fun `Expected full todo list sorted by name, because search query is empty`() =
        runBlocking {
            val searchQuery = ""
            Mockito.`when`(fakeTodoRepository.getSortedTodoList(searchQuery, SortOrder.BY_NAME))
                .thenReturn(
                    flow {
                        emit(todoList.sortedBy { todo -> todo.description }.takeIf { todo ->
                            todo.description.lowercase().contains(searchQuery.lowercase())
                        })
                    }
                )

            val sortedTodoList =
                todoInteractor.getSortedTodoList(searchQuery, SortOrder.BY_NAME).first()

            for (i in 0..sortedTodoList.size - 2) {
                assert(sortedTodoList[i].description < sortedTodoList[i + 1].description && sortedTodoList.size == 3)
            }
        }

    @Test
    fun `Expected todo list of one item 'Todo A' sorted by name, because search query is 'a'`() =
        runBlocking {
            val searchQuery = "a"
            Mockito.`when`(fakeTodoRepository.getSortedTodoList(searchQuery, SortOrder.BY_NAME))
                .thenReturn(
                    flow {
                        emit(todoList.sortedBy { todo -> todo.description }.takeIf { todo ->
                            todo.description.lowercase().contains(searchQuery.lowercase())
                        })
                    }
                )

            val sortedTodoList =
                todoInteractor.getSortedTodoList(searchQuery, SortOrder.BY_NAME).first()

            assert(sortedTodoList[0] == todoA)
        }

    @Test
    fun `Expected full todo list sorted by name, because search query is 'Todo'`() =
        runBlocking {
            val searchQuery = "Todo"
            Mockito.`when`(fakeTodoRepository.getSortedTodoList(searchQuery, SortOrder.BY_NAME))
                .thenReturn(
                    flow {
                        emit(todoList.sortedBy { todo -> todo.description }.takeIf { todo ->
                            todo.description.lowercase().contains(searchQuery.lowercase())
                        })
                    }
                )

            val sortedTodoList =
                todoInteractor.getSortedTodoList(searchQuery, SortOrder.BY_NAME).first()

            for (i in 0..sortedTodoList.size - 2) {
                assert(sortedTodoList[i].description < sortedTodoList[i + 1].description && sortedTodoList.size == 3)
            }
        }

    @Test
    fun `Expected full todo list sorted by date, because search query is empty`() = runBlocking {
        val searchQuery = ""
        Mockito.`when`(fakeTodoRepository.getSortedTodoList(searchQuery, SortOrder.BY_DATE))
            .thenReturn(
                flow {
                    emit(todoList.sortedBy { todo -> todo.timestamp }.takeIf { todo ->
                        todo.description.lowercase().contains(searchQuery.lowercase())
                    })
                }
            )

        val sortedTodoList =
            todoInteractor.getSortedTodoList(searchQuery, SortOrder.BY_DATE).first()

        for (i in 0..sortedTodoList.size - 2) {
            assert(sortedTodoList[i].timestamp < sortedTodoList[i + 1].timestamp && sortedTodoList.size == 3)
        }
    }

    @Test
    fun `Expected todo list of one item 'Todo B' sorted by date, because search query is 'b'`() =
        runBlocking {
            val searchQuery = "b"
            Mockito.`when`(fakeTodoRepository.getSortedTodoList(searchQuery, SortOrder.BY_DATE))
                .thenReturn(
                    flow {
                        emit(todoList.sortedBy { todo -> todo.timestamp }.takeIf { todo ->
                            todo.description.lowercase().contains(searchQuery.lowercase())
                        })
                    }
                )

            val sortedTodoList =
                todoInteractor.getSortedTodoList(searchQuery, SortOrder.BY_DATE).first()

            assert(sortedTodoList[0] == todoB)
        }

    @Test
    fun `Expected full todo list sorted by date, because search query is 'Todo'`() = runBlocking {
        val searchQuery = "Todo"
        Mockito.`when`(fakeTodoRepository.getSortedTodoList(searchQuery, SortOrder.BY_DATE))
            .thenReturn(
                flow {
                    emit(todoList.sortedBy { todo -> todo.timestamp }.takeIf { todo ->
                        todo.description.lowercase().contains(searchQuery.lowercase())
                    })
                }
            )

        val sortedTodoList =
            todoInteractor.getSortedTodoList(searchQuery, SortOrder.BY_DATE).first()

        for (i in 0..sortedTodoList.size - 2) {
            assert(sortedTodoList[i].timestamp < sortedTodoList[i + 1].timestamp && sortedTodoList.size == 3)
        }
    }
}