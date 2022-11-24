package com.example.mynote.ui.todo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.R
import com.example.mynote.adapter.TodoListAdapter
import com.example.mynote.common.Utils
import com.example.mynote.databinding.FragmentTodoListBinding
import com.example.mynote.model.Todo
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var viewModel: TodoViewModel

    private lateinit var todoListAdapter: TodoListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        setupMenu()
        setupSearchView()
        setupRecyclerView()

        binding.btnAddTodo.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_todoAddFragment)
        }

        //for hide keyboard view
        Utils.hideKeyboard(requireActivity())

    }

    private fun setupMenu() {
        binding.toolbarTodo.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_removal_todo -> {
                    confirmRemoval()
                    true
                }
                else -> onOptionsItemSelected(item)
            }
        }
    }

    private fun setupSearchView() {
        binding.svTodo.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchTodo(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchTodo(it) }
                return true
            }
        })
    }

    private fun searchTodo(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchTodo(searchQuery).observe(viewLifecycleOwner) { list ->
            list?.let {
                todoListAdapter.differ.submitList(list)
            }
        }
    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter()

        binding.apply {
            rvTodoList.adapter = todoListAdapter
            rvTodoList.layoutManager = LinearLayoutManager(requireActivity())
            rvTodoList.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
            swipeToDelete(rvTodoList)
        }

        todoListAdapter.setOnItemClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToTodoAddFragment(it)
            findNavController().navigate(action)
        }

        todoListAdapter.onTodoCheckedChangeListener = { item, isChecked ->
            val todo = Todo(
                item.id,
                item.title,
                isChecked
            )
            viewModel.updateTodo(todo)
        }

        viewModel.todos.observe(viewLifecycleOwner) { todos ->
            todoListAdapter.differ.submitList(todos)
        }

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val itemToDelete =
                        todoListAdapter.differ.currentList[viewHolder.adapterPosition]
                    viewModel.deleteTodo(itemToDelete)
                    todoListAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    restoreDeleteTodo(viewHolder.itemView, itemToDelete)
                }
            }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeleteTodo(view: View, deleteItem: Todo) {
        val snackBar = Snackbar.make(
            view,
            "Delete ${deleteItem.title}",
            Snackbar.LENGTH_LONG
        )
        snackBar.apply {
            setAction("Undo") {
                viewModel.insertTodo(deleteItem)
            }
        }.show()
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setNegativeButton("No") { _, _ -> }
            setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllTodos()
                Toast.makeText(
                    requireContext(),
                    "Deleted All Todos",
                    Toast.LENGTH_LONG
                ).show()
            }
            setTitle("Delete All Todos")
            setMessage("Are you sure want to delete all?")
        }.show()
    }


}