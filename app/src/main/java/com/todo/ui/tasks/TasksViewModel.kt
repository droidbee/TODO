package com.todo.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.todo.db.TaskEntity
import com.todo.repository.TaskRepository
import com.todo.ui.createtask.CreateTaskViewModel

class TasksViewModel(private val repository: TaskRepository):ViewModel() {

    private val TAG= TasksViewModel::class.java.simpleName

    //Variable to hold list of tasks Livedata
    private val _tasks=repository.getAllTasks()
    val tasks: LiveData<List<TaskEntity>>
        get() = _tasks
}