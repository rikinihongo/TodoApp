package com.sonpxp.todoapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object TaskList

@Serializable
data class TaskDetail(val taskId: Long = -1L)