package com.todo.ui.createtask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.todo.R
import com.todo.databinding.FragmentCreateTaskBinding


class CreateTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = FragmentCreateTaskBinding.inflate(inflater)
        return binding.root
    }

}