package com.sonpxp.todoapp.data.repository

import com.sonpxp.todoapp.data.local.TaskDao
import com.sonpxp.todoapp.data.mapper.toDomain
import com.sonpxp.todoapp.data.mapper.toEntity
import com.sonpxp.todoapp.domain.model.Task
import com.sonpxp.todoapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTaskById(id: Long): Flow<Task?> {
        return taskDao.getTaskById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun insertTask(task: Task): Long {
        return taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    override fun getCompletedTasks(): Flow<List<Task>> {
        return taskDao.getCompletedTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getIncompleteTasks(): Flow<List<Task>> {
        return taskDao.getIncompleteTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}