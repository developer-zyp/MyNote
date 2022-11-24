package com.example.mynote.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynote.common.AppExtension.setFullScreen
import com.example.mynote.common.Utils
import com.example.mynote.databinding.FragmentTodoAddBinding
import com.example.mynote.model.Todo

class TodoAddFragment : DialogFragment() {

    private lateinit var binding: FragmentTodoAddBinding
    private lateinit var viewModel: TodoViewModel

    private val args by navArgs<TodoAddFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodoAddBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFullScreen()
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        setupUI()

    }

    private fun setupUI() {

        if (args.currentTodo == null) {
            binding.toolbar.title = "Add Todo Task"
        } else {
            binding.toolbar.title = "Update Todo Task"
            binding.edtTodoAddTitle.setText(args.currentTodo!!.title)
        }

        binding.apply {
            btnTodoAdd.setOnClickListener {
                val txtTitle = edtTodoAddTitle.text.toString()
                if (txtTitle.isNotEmpty()) {
                    val todo = Todo(
                        args.currentTodo?.id ?: 0,
                        txtTitle
                    )

                    if (args.currentTodo == null) {
                        viewModel.insertTodo(todo)
                        Utils.showToast("Todo Task Added")
                    } else {
                        viewModel.updateTodo(todo)
                        Utils.showToast("Todo Task Updated")
                    }
                    findNavController().popBackStack()
                } else {
                    Utils.showToast("Please enter the task")
                }
            }
        }

    }

}