package com.example.mynote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynote.database.DBHelper
import com.example.mynote.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NoteViewModel : ViewModel() {

    private val dbHelper = DBHelper()

    val getAllNotes = dbHelper.notesFlow.asLiveData()
    val sortByHighPriority = dbHelper.sortByHighPriority.asLiveData()
    val sortByLowPriority = dbHelper.sortByLowPriority.asLiveData()

    fun insertData(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.deleteNote(note)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.deleteAllNotes()
        }
    }

    fun searchNote(searchQuery: String) =
        dbHelper.searchNote(searchQuery).asLiveData()

    fun getCurrentDate(): String {
        val current = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(current)
    }

}