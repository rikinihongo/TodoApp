package com.sonpxp.todoapp.domain.repository

import com.sonpxp.todoapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    fun getTaskById(id: Long): Flow<Task?>
    suspend fun insertTask(task: Task): Long
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAllTasks()
    fun getCompletedTasks(): Flow<List<Task>>
    fun getIncompleteTasks(): Flow<List<Task>>
}