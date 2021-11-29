package com.todo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A database that stores TaskEntity information.
 * And a global method to get access to the database.
 **/

@Database(
    entities = [TaskEntity::class],
    exportSchema = false,
    version = 5
)
abstract class TaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}