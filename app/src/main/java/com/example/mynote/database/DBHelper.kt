package com.example.mynote.database

import com.example.mynote.AppInstance
import com.example.mynote.model.Note
import com.example.mynote.model.Todo
import kotlinx.coroutines.flow.Flow

class DBHelper {

    private val db: AppDatabase = AppDatabase.getInstance(AppInstance.getInstance())

    //region Note CRUD
    val notesFlow: Flow<List<Note>>
        get() = db.noteDao().getAll()

    val sortByHighPriority: Flow<List<Note>>
        get() = db.noteDao().sortByPriorityHigh()

    val sortByLowPriority: Flow<List<Note>>
        get() = db.noteDao().sortByPriorityLow()

    suspend fun insertNote(note: Note) {
        db.noteDao().insertData(note)
    }

    suspend fun updateNote(note: Note) {
        db.noteDao().updateData(note)
    }

    suspend fun deleteNote(note: Note) {
        db.noteDao().deleteData(note)
    }

    suspend fun deleteAllNotes() {
        db.noteDao().deleteAll()
    }

    fun searchNote(searchQuery: String) =
        db.noteDao().searchData(searchQuery)

    //endregion

    //region TODOs CRUD
    val todosFlow: Flow<List<Todo>>
        get() = db.todoDao().getAll()

    suspend fun insertTodo(todo: Todo) {
        db.todoDao().insertData(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        db.todoDao().updateData(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        db.todoDao().deleteData(todo)
    }

    suspend fun deleteAllTodos() {
        db.todoDao().deleteAll()
    }

    fun searchTodo(searchQuery: String) =
        db.todoDao().searchData(searchQuery)

    //endregion


}