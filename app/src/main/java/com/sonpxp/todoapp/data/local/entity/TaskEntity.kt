package com.sonpxp.todoapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sonpxp.todoapp.domain.model.Priority
import java.util.Date

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdDate: Date = Date(),
    val dueDate: Date? = null,
    val priority: Priority = Priority.MEDIUM
)