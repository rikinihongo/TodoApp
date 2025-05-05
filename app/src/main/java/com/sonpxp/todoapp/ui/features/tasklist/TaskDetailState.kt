package com.sonpxp.todoapp.ui.features.tasklist

import com.sonpxp.todoapp.domain.model.Priority
import com.sonpxp.todoapp.domain.model.Task
import java.util.Date

data class TaskDetailState(
    val task: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val dueDate: Date? = null,
    val priority: Priority = Priority.MEDIUM,
    val isSaving: Boolean = false
)