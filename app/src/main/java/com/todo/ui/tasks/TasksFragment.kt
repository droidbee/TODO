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
import com.todo.R
import com.todo.databinding.FragmentTasksBinding
import com.todo.db.TaskDatabase
import com.todo.repository.TaskRepository
import com.todo.ui.createtask.CreateTaskViewModel
import com.todo.ui.createtask.CreateTaskViewModelFactory


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
        val viewModelFactory = CreateTaskViewModelFactory(repository)
        // Get a reference to the ViewModel associated with this fragment.
        val tasksViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CreateTaskViewModel::class.java)

        binding.viewmodel=tasksViewModel

        adapter = TaskAdapter(TaskAdapter.OnClickListener {

            Log.d("TASK FRAGMENT", "Task clicked")


        })

        tasksViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("TASKSSS",it.toString())
                adapter.submitList(it)
            }
        })

        binding.recyclerView.adapter=adapter
        return binding.root
    }

}