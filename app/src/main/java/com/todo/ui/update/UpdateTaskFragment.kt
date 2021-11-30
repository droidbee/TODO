package com.todo.ui.update

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.todo.R
import com.todo.databinding.FragmentUpdateTaskBinding
import com.todo.db.TaskEntity
import com.todo.ui.create.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateTaskFragment : Fragment() {

    private val updateTaskViewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = FragmentUpdateTaskBinding.inflate(inflater)
        val task_args = UpdateTaskFragmentArgs.fromBundle(requireArguments())


        binding.apply {
            task = task_args.task

            calenderIcon.setOnClickListener {
                calendarClickListener(updatedateEt)
            }

            updatedateEt.setOnClickListener {
                calendarClickListener(updatedateEt)
            }

            upadteBtn.setOnClickListener {
                if (TextUtils.isEmpty((updatecreateTaskEt.text))) {
                    Toast.makeText(
                        requireContext(),
                        R.string.task_cannot_be_empty,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }

                val task = TaskEntity(
                    task_args.task.taskId,
                    updatecreateTaskEt.text.toString(),
                    updatedateEt.text.toString()
                )

                updateTaskViewModel.onUpdateButtonClick(task)
            }
        }

        //Observe status message to trigger navigation
        updateTaskViewModel.insertionOrUpdationSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(requireContext(), R.string.task_updated, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(UpdateTaskFragmentDirections.actionUpdateTaskFragmentToTasksFragment())
                updateTaskViewModel.doneNavigating()
            }
        })
        return binding.root
    }

    /**
     * Function to open Calender and set the selected date in EditText field.
     */

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
