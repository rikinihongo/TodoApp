package com.sonpxp.todoapp.domain.model

import java.util.Date

data class Task(
    val id: Long = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val createdDate: Date = Date(),
    val dueDate: Date? = null,
    val priority: Priority = Priority.MEDIUM
)

enum class Priority {
    LOW, MEDIUM, HIGH
}