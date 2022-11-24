package com.example.mynote.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynote.database.DBHelper
import com.example.mynote.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {

    private val dbHelper = DBHelper()

    val todos = dbHelper.todosFlow.asLiveData()

    fun insertTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.insertTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.deleteTodo(todo)
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            dbHelper.deleteAllTodos()
        }
    }

    fun searchTodo(searchQuery: String) =
        dbHelper.searchTodo(searchQuery).asLiveData()


}