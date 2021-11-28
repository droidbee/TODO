package com.todo.ui.create

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.*
import com.todo.db.TaskEntity
import com.todo.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val TAG =TaskViewModel::class.java.simpleName


    //Value which is observed to check if the insertion is success
    //If true ,then displays a toast showing data insertion success message
    // and triggers navigation to TasksFragment
    private val _insertionOrUpdationSuccess = MutableLiveData<Boolean?>()
    val insertionOrUpdationSuccess: LiveData<Boolean?>
        get() = _insertionOrUpdationSuccess

    //Exception message to be observed
    private  val _exceptionMessage = MutableLiveData<String?>()
    val exceptionMessage:LiveData<String?>
        get() = _exceptionMessage

    //Variable to hold list of tasks Livedata
    private val _tasks = taskRepository.getAllTasks()
    val tasks: LiveData<List<TaskEntity>>
        get() = _tasks


    /**
     * Called when Add button in [CreateTaskFragment] is clicked
     *
     */

    fun onAddButtonClick(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                insert(task)
                _insertionOrUpdationSuccess.postValue(true)
            } catch (exception: SQLiteConstraintException) {
                _exceptionMessage.postValue("Unique Constraint Exception")
            }catch(exception:Exception){
                Log.d(TAG,exception.message.toString())
            }
        }
    }

    private suspend fun insert(task: TaskEntity) {
        taskRepository.insert(task)
    }


    /**
     * Called when Update button in [UpdateTaskFragment] is clicked
     *
     */
    fun onUpdateButtonClick(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                update(task)
                _insertionOrUpdationSuccess.postValue(true)
            }catch(exception:Exception){
                Log.d(TAG, exception.message.toString())
            }
        }
    }

    private suspend fun update(task: TaskEntity) {
        taskRepository.update(task)
    }


    fun onDeleteSwipe(task:TaskEntity){
        viewModelScope.launch(Dispatchers.IO) {
            delete(task)
        }
    }

    private suspend fun delete(task: TaskEntity) {
        taskRepository.delete(task)
    }

    /**
     * Call this immediately after navigating to [TasksFragment]
     */
    fun doneNavigating() {
        _insertionOrUpdationSuccess.value = null
    }
}

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 */
class TaskViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}