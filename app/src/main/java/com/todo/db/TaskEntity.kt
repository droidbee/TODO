package com.todo.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "tasks",
    indices =[Index(value=["taskName"],unique=true)]
)
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val taskId:Int,
    val taskName:String,
    val taskDate:String,
    val taskCreator:String="Neeraja"
):Serializable
