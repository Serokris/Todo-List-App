package com.example.data.source.local

import androidx.room.*
import com.example.data.models.TodoEntity
import com.example.domain.common.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Update
    suspend fun update(todo: TodoEntity)

    @Query("DELETE FROM `todo-table`")
    suspend fun deleteAll()

    @Query("DELETE FROM `todo-table` WHERE isCompleted = 1")
    suspend fun deleteAllCompleted()

    @Query("SELECT * FROM `todo-table` WHERE isCompleted = 1")
    fun getAllCompleted(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM `todo-table` WHERE isCompleted = 0")
    fun getAllUncompleted(): Flow<List<TodoEntity>>

    fun getSortedTodoList(searchQuery: String, sortOrder: SortOrder): Flow<List<TodoEntity>> {
        return when (sortOrder) {
            SortOrder.BY_NAME -> getTodoListSortedByName(searchQuery)
            SortOrder.BY_DATE -> getTodoListSortedByDateCreated(searchQuery)
        }
    }

    @Query("SELECT * FROM `todo-table` WHERE description LIKE '%' || :searchQuery || '%' ORDER BY description ASC")
    fun getTodoListSortedByName(searchQuery: String): Flow<List<TodoEntity>>

    @Query("SELECT * FROM `todo-table` WHERE description LIKE '%' || :searchQuery || '%' ORDER BY timestamp ASC")
    fun getTodoListSortedByDateCreated(searchQuery: String): Flow<List<TodoEntity>>
}