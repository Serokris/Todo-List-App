package com.example.to_dolistapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert
    suspend fun insert(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Query ("DELETE FROM `todo-table`")
    suspend fun deleteAll()

    @Query ("DELETE FROM `todo-table` WHERE isCompleted = 1")
    suspend fun deleteAllCompleted()

    @Query ("SELECT * FROM `todo-table` WHERE isCompleted = 1")
    fun getAllCompleted(): LiveData<List<Todo>>

    @Query ("SELECT * FROM `todo-table` WHERE isCompleted = 0")
    fun getAllUncompleted() : LiveData<List<Todo>>

    fun getSortedTodoList(sortOrder: SortOrder): Flow<List<Todo>> {
        return when (sortOrder) {
            SortOrder.BY_NAME -> getTodoListSortedByName()
            SortOrder.BY_DATE -> getTodoListSortedByDateCreated()
        }
    }

    @Query("SELECT * FROM `todo-table` ORDER BY description ASC")
    fun getTodoListSortedByName(): Flow<List<Todo>>

    @Query("SELECT * FROM `todo-table` ORDER BY timestamp ASC")
    fun getTodoListSortedByDateCreated(): Flow<List<Todo>>

    @Query ("SELECT * FROM `todo-table` WHERE description LIKE :searchQuery ORDER BY timestamp DESC")
    fun databaseSearch(searchQuery: String): LiveData<List<Todo>>
}