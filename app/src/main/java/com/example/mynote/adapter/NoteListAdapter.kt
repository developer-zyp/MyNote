package com.example.mynote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.AppInstance
import com.example.mynote.R
import com.example.mynote.databinding.ItemNoteBinding
import com.example.mynote.model.Note

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = differ.currentList[position]
        holder.binding.apply {
            itemNoteTitle.text = currentItem.title
            itemNoteContent.text = currentItem.content
            itemNoteDate.text = currentItem.date
            when (currentItem.priority) {
                "High" -> {
                    itemCvPriority.setCardBackgroundColor(
                        ContextCompat.getColor(
                            AppInstance.getInstance(),
                            R.color.red_500
                        )
                    )
                }
                "Medium" -> {
                    itemCvPriority.setCardBackgroundColor(
                        ContextCompat.getColor(
                            AppInstance.getInstance(),
                            R.color.yellow_500
                        )
                    )
                }
                "Low" -> {
                    itemCvPriority.setCardBackgroundColor(
                        ContextCompat.getColor(
                            AppInstance.getInstance(),
                            R.color.green_500
                        )
                    )
                }
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { it1 -> it1(currentItem) }

        }

    }

    private val differCallBack = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Note) -> Unit)? = null
    fun setOnItemClickListener(listener: (Note) -> Unit) {
        onItemClickListener = listener
    }

}