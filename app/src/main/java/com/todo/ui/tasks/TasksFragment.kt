package com.todo.ui.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.todo.R
import com.todo.databinding.FragmentTasksBinding


class TasksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = FragmentTasksBinding.inflate(inflater)

        binding.fab.setOnClickListener {
            this.findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToCreateTaskFragment())
        }

        return binding.root
    }

}