package com.example.mynote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.databinding.ItemToDoBinding
import com.example.mynote.model.Todo

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemToDoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = differ.currentList[position]
        holder.binding.apply {

            checkBox.isChecked = currentItem.isDone
            tvTodoTitle.text = currentItem.title

            checkBox.setOnCheckedChangeListener { compoundButton, isChecked ->
                onTodoCheckedChangeListener?.invoke(currentItem, isChecked)
            }

        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it1 -> it1(currentItem) }

        }

    }

    private val differCallBack = object : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onTodoCheckedChangeListener: ((Todo, Boolean) -> Unit)? = null

    private var onItemClickListener: ((Todo) -> Unit)? = null
    fun setOnItemClickListener(listener: (Todo) -> Unit) {
        onItemClickListener = listener
    }

}