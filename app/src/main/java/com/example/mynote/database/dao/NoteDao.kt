package com.example.mynote.database.dao

import androidx.room.*
import com.example.mynote.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAll(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(note: Note)

    @Update
    suspend fun updateData(note: Note)

    @Delete
    suspend fun deleteData(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery")
    fun searchData(searchQuery: String): Flow<List<Note>>

    @Query(
        "SELECT * FROM note_table ORDER BY CASE " +
                "WHEN priority LIKE 'H%' THEN 1 " +
                "WHEN priority LIKE 'M%' THEN 2 " +
                "WHEN priority LIKE 'L%' THEN 3 END"
    )
    fun sortByPriorityHigh(): Flow<List<Note>>

    @Query(
        "SELECT * FROM note_table ORDER BY CASE " +
                "WHEN priority LIKE 'L%' THEN 1 " +
                "WHEN priority LIKE 'M%' THEN 2 " +
                "WHEN priority LIKE 'H%' THEN 3 END"
    )
    fun sortByPriorityLow(): Flow<List<Note>>

}