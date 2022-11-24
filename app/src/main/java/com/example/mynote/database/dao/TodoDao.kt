package com.example.mynote.database.dao

import androidx.room.*
import com.example.mynote.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table ORDER BY isDone ASC")
    fun getAll(): Flow<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(todo: Todo)

    @Update
    suspend fun updateData(todo: Todo)

    @Delete
    suspend fun deleteData(todo: Todo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
    fun searchData(searchQuery: String): Flow<List<Todo>>

}