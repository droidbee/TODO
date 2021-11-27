package com.todo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks"
)
data class TaskEntity(

    @PrimaryKey
    val taskName:String,
    val taskDate:String,
    val taskCreator:String="Neeraja"
)
