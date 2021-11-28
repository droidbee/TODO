package com.todo.ui.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.todo.R
import com.todo.databinding.FragmentTasksBinding
import com.todo.db.TaskDatabase
import com.todo.repository.TaskRepository
import com.todo.ui.create.TaskViewModel
import com.todo.ui.create.TaskViewModelFactory


class TasksFragment : Fragment() {

    private lateinit var adapter:TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = FragmentTasksBinding.inflate(inflater)

        binding.fab.setOnClickListener {
            this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToCreateTaskFragment())
        }

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val db = TaskDatabase.getInstance(application)
        val repository = TaskRepository(db)
        val viewModelFactory = TaskViewModelFactory(repository)
        // Get a reference to the ViewModel associated with this fragment.
        val tasksViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TaskViewModel::class.java)

        binding.viewmodel=tasksViewModel

        adapter = TaskAdapter(TaskAdapter.OnClickListener {
            findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToUpdateTaskFragment(it))
        })
        adapter.setHasStableIds(true)


        tasksViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.recyclerView.adapter=adapter



        ItemTouchHelper(object  : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskEntry = adapter.currentList[position]
                tasksViewModel.onDeleteSwipe(taskEntry)

                Snackbar.make(binding.root, R.string.task_deleted, Snackbar.LENGTH_LONG).apply {
                    setAction(R.string.undo){
                        tasksViewModel.onAddButtonClick(taskEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)

        return binding.root
    }
}