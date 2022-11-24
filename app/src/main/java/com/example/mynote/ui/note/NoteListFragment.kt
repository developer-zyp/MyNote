package com.example.mynote.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynote.R
import com.example.mynote.adapter.NoteListAdapter
import com.example.mynote.common.Utils
import com.example.mynote.databinding.FragmentNoteListBinding
import com.example.mynote.model.Note
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class NoteListFragment : Fragment() {

    private lateinit var binding: FragmentNoteListBinding
    private lateinit var viewModel: NoteViewModel

    private lateinit var noteListAdapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        setupMenu()
        setupSearchView()
        setupRecyclerView()

        binding.btnAddNote.setOnClickListener {
//            val action = NoteListFragmentDirections.actionNoteListFragmentToNoteAddFragment()
            findNavController().navigate(R.id.action_noteListFragment_to_noteAddFragment)
        }

        //for hide keyboard view
        Utils.hideKeyboard(requireActivity())

    }


    private fun setupMenu() {
        binding.toolbarNoteList.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_delete_all_note -> {
                    confirmRemoval()
                    true
                }
                R.id.menu_high_priority -> {
                    viewModel.sortByHighPriority.observe(viewLifecycleOwner) {
                        noteListAdapter.differ.submitList(it)
                    }
                    true
                }
                R.id.menu_low_priority -> {
                    viewModel.sortByLowPriority.observe(viewLifecycleOwner) {
                        noteListAdapter.differ.submitList(it)
                    }
                    true
                }
                else -> super.onContextItemSelected(item)
            }
        }
    }

    private fun setupSearchView() {
        binding.svNoteList.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { searchNote(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { searchNote(it) }
                    return true
                }
            }
        )
    }

    private fun searchNote(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchNote(searchQuery).observe(viewLifecycleOwner) { list ->
            list?.let {
                noteListAdapter.differ.submitList(it)
            }
        }
    }

    private fun setupRecyclerView() {
        noteListAdapter = NoteListAdapter()
        noteListAdapter.setOnItemClickListener {
            val action = NoteListFragmentDirections.actionNoteListFragmentToNoteAddFragment(it)
            findNavController().navigate(action)
        }

        binding.apply {
            rvNoteList.adapter = noteListAdapter
            rvNoteList.layoutManager = StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL
            )
            rvNoteList.itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }

            swipeToDelete(rvNoteList)
        }

        viewModel.getAllNotes.observe(viewLifecycleOwner) { notes ->
            noteListAdapter.differ.submitList(notes)
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
                        noteListAdapter.differ.currentList[viewHolder.adapterPosition]
                    viewModel.deleteNote(itemToDelete)
                    noteListAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    restoreDeleteNote(viewHolder.itemView, itemToDelete)
                }
            }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeleteNote(view: View, deletedNote: Note) {
        val snackBar = Snackbar.make(
            view,
            "Deleted ${deletedNote.title}",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            viewModel.insertData(deletedNote)
        }
        snackBar.show()
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setNegativeButton("No") { _, _ -> }
            setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllNotes()
                Utils.showToast("Deleted All Notes")
            }
            setTitle("Delete Notes")
            setMessage("Are you sure want to delete?")
        }.show()

    }


}