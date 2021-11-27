package com.todo.ui.createtask

import android.app.DatePickerDialog
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.*
import com.todo.db.TaskDatabase
import com.todo.db.TaskEntity
import com.todo.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CreateTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val TAG = CreateTaskViewModel::class.java.simpleName


    //Value which is observed to check if the insertion is success
    //If true ,then displays a toast showing data insertion success message
    // and triggers navigation to TasksFragment
    private val _insertionSuccess = MutableLiveData<Boolean?>()
    val insertionSuccess: LiveData<Boolean?>
        get() = _insertionSuccess

    //Exception message to be observed
    private  val _exceptionMessage = MutableLiveData<String?>()
    val exceptionMessage:LiveData<String?>
        get() = _exceptionMessage

    //Variable to hold list of tasks Livedata
    private val _tasks = taskRepository.getAllTasks()
    val tasks: LiveData<List<TaskEntity>>
        get() = _tasks




    fun onAddButtonClick(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                insert(task)
                _insertionSuccess.postValue(true)

            } catch (exception: SQLiteConstraintException) {

                Log.d(TAG, "Error occured. Try Again later!" + exception.message.toString())
                _exceptionMessage.postValue("Unique Constraint Exception")

            }catch(exception:Exception){
                Log.d(TAG, "Error occured. Try Again later!" + exception.message.toString())
            }
        }

    }

    private suspend fun insert(task: TaskEntity) {
        taskRepository.insert(task)
    }


    /**
     * Call this immediately after navigating to [TasksFragment]
     */
    fun doneNavigating() {
        _insertionSuccess.value = null
    }


}

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the key for the night and the SleepDatabaseDao to the ViewModel.
 */
class CreateTaskViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)) {
            return CreateTaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}