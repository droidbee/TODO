package com.todo.ui.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.todo.R
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
        val viewModelFactory = TaskViewModelFactory(repository)
        // Get a reference to the ViewModel associated with this fragment.
        val createTaskViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(TaskViewModel::class.java)


        binding.apply {

            viewmodel = createTaskViewModel

            calenderIcon.setOnClickListener {
                calendarClickListener(dateEt)
            }

            dateEt.setOnClickListener {
                calendarClickListener(dateEt)
            }

            addBtn.setOnClickListener {
                if (TextUtils.isEmpty((createTaskEt.text))) {
                    Toast.makeText(requireContext(), R.string.task_cannot_be_empty, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty((dateEt.text))) {
                    Toast.makeText(requireContext(), R.string.select_date, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                val task = TaskEntity(
                    0,
                    createTaskEt.text.toString(),
                    dateEt.text.toString(),
                )
                createTaskViewModel.onAddButtonClick(task)
            }
        }


        createTaskViewModel.insertionOrUpdationSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(requireContext(), "Task added successfully!", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(CreateTaskFragmentDirections.actionCreateTaskFragmentToTasksFragment())
                createTaskViewModel.doneNavigating()
            }
        })


        createTaskViewModel.exceptionMessage.observe(viewLifecycleOwner, Observer {
            if(it=="Unique Constraint Exception"){
                Toast.makeText(requireContext(), R.string.task_exists, Toast.LENGTH_SHORT)
                    .show()
            }
        })
        return binding.root
    }


    private fun calendarClickListener(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog =
            DatePickerDialog(
                requireContext(), DatePickerDialog.OnDateSetListener
                { view, year, monthOfYear, dayOfMonth ->
                    val taskDate = "" + dayOfMonth + " - " + (monthOfYear + 1) + " - " + year
                    editText.setText(taskDate)
                }, year, month, day
            )
        datePickerDialog.datePicker.minDate = Date().time
        datePickerDialog.show()
    }

}