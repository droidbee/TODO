package com.todo.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.todo.db.TaskDatabase
import com.todo.db.TaskEntity

class TaskRepository (val taskDb: TaskDatabase){

    suspend fun insert(taskEntity: TaskEntity)=
        taskDb.taskDao.insert(taskEntity)

    suspend fun update(taskEntity: TaskEntity)=
        taskDb.taskDao.update(taskEntity)

    suspend fun delete(taskEntity: TaskEntity)=
        taskDb.taskDao.delete(taskEntity)

    fun getAllTasks() :LiveData<List<TaskEntity>> = taskDb.taskDao.getAllTasks()
}