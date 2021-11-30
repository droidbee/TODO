package com.todo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.todo.R
import com.todo.databinding.FragmentTasksBinding
import com.todo.ui.create.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var adapter:TaskAdapter
    private val taskViewModel:TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTasksBinding.inflate(inflater)

        binding.fab.setOnClickListener {
            this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToCreateTaskFragment())
        }


        adapter = TaskAdapter(TaskAdapter.OnClickListener {
            findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToUpdateTaskFragment(it))
        })
        adapter.setHasStableIds(true)



        binding.apply{
            viewmodel=taskViewModel
            recyclerView.adapter=adapter
        }


        //Code to delete Task on Swipe
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
                taskViewModel.onDeleteSwipe(taskEntry)

                Snackbar.make(binding.root, R.string.task_deleted, Snackbar.LENGTH_LONG).apply {
                    setAction(R.string.undo){
                        taskViewModel.onAddButtonClick(taskEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)


        //Observe tasks from viewmodel to display on the home screen
        taskViewModel.tasks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}