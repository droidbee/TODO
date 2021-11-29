package com.todo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.todo.db.TaskDao
import com.todo.db.TaskDatabase
import com.todo.db.TaskEntity
import javax.inject.Inject

class TaskRepository @Inject constructor(val taskDao: TaskDao){

    suspend fun insert(taskEntity: TaskEntity)=
        taskDao.insert(taskEntity)

    suspend fun update(taskEntity: TaskEntity)=
        taskDao.update(taskEntity)

    suspend fun delete(taskEntity: TaskEntity)=
        taskDao.delete(taskEntity)

    fun getAllTasks() :LiveData<List<TaskEntity>> = taskDao.getAllTasks()
}