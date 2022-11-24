package com.example.mynote.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynote.R
import com.example.mynote.common.Utils
import com.example.mynote.databinding.FragmentNoteAddBinding
import com.example.mynote.model.Note
import com.google.android.material.chip.Chip

class NoteAddFragment : Fragment() {

    private lateinit var binding: FragmentNoteAddBinding
    private lateinit var viewModel: NoteViewModel

    private val args by navArgs<NoteAddFragmentArgs>()

    var priority = "Low"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        setupUI()

    }

    private fun setupUI() {
        binding.apply {

            if (args.currentNote == null) {
                toolbar.title = "Add Note"
            } else {
                toolbar.title = "Update Note"
                edtNoteTitle.setText(args.currentNote!!.title)
                edtNoteContent.setText(args.currentNote!!.content)
                when (args.currentNote!!.priority) {
                    "High" -> {
                        chipHigh.isChecked = true
                        priority = "High"
                    }
                    "Medium" -> {
                        chipMedium.isChecked = true
                        priority = "Medium"
                    }
                    "Low" -> {
                        chipLow.isChecked = true
                        priority = "Low"
                    }
                }
            }

            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_note_save -> {
                        saveNoteToDB()
                        true
                    }
                    else -> {
                        super.onOptionsItemSelected(item)
                    }
                }
            }

            chipGroupNoteAdd.setOnCheckedChangeListener { group, checkedId ->
                priority = group.findViewById<Chip>(checkedId).text.toString()
//                Utils.showToast(priority)
            }

        }

    }


    private fun saveNoteToDB() {
        binding.apply {
            val title = edtNoteTitle.text.toString()
            val content = edtNoteContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(
                    args.currentNote?.id ?: 0,
                    title,
                    content,
                    viewModel.getCurrentDate(),
                    priority
                )

                if (args.currentNote == null) {
                    viewModel.insertData(note)
                    Utils.showToast("Note added")
                } else {
                    viewModel.updateNote(note)
                    Utils.showToast("Note updated")
                }
                findNavController().popBackStack()
            } else {
                Utils.showToast("Please enter the fields")
            }
        }
    }


}