package com.todo.ui.createtask

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.todo.databinding.FragmentCreateTaskBinding
import com.todo.db.TaskDatabase
import com.todo.db.TaskEntity
import com.todo.repository.TaskRepository
import java.util.*


class CreateTaskFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCreateTaskBinding.inflate(inflater)

        // Create an instance of the ViewModel Factory.
        val application = requireNotNull(this.activity).application
        val db = TaskDatabase.getInstance(application)
        val repository = TaskRepository(db)
        val viewModelFactory = CreateTaskViewModelFactory(repository)
        // Get a reference to the ViewModel associated with this fragment.
        val createTaskViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(CreateTaskViewModel::class.java)


        binding.apply {

            viewmodel = createTaskViewModel

            var taskDate: String


            calenderIcon.setOnClickListener {

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog =
                    DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener
                    { view, year, monthOfYear, dayOfMonth ->
                        taskDate = "" + dayOfMonth + " - " + (monthOfYear + 1) + " - " + year
                        dateEt.setText(taskDate)
                    }, year, month, day
                    )
                datePickerDialog.datePicker.minDate=Date().time
                datePickerDialog.show()

            }
            addBtn.setOnClickListener {

                if (TextUtils.isEmpty((createTaskEt.text))) {
                    Toast.makeText(requireContext(), "Task cannot be empty!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val task = TaskEntity(
                    createTaskEt.text.toString(),
                    dateEt.text.toString(),
                )
                createTaskViewModel.onAddButtonClick(task)
            }
        }

        createTaskViewModel.insertionSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(requireContext(), "Task added successfully!", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(CreateTaskFragmentDirections.actionCreateTaskFragmentToTasksFragment())
                createTaskViewModel.doneNavigating()
            }
        })

        createTaskViewModel.exceptionMessage.observe(viewLifecycleOwner, Observer {
            if(it=="Unique Constraint Exception"){
                Toast.makeText(requireContext(), "Task already added. Please enter a different task.", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        return binding.root
    }

}