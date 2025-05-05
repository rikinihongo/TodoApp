package com.sonpxp.todoapp.ui.features.taskdetail

import com.sonpxp.todoapp.domain.model.Task

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
