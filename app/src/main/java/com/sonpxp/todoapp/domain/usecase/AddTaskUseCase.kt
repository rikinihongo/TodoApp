package com.sonpxp.todoapp.domain.usecase

import com.sonpxp.todoapp.domain.model.Task
import com.sonpxp.todoapp.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long {
        return taskRepository.insertTask(task)
    }
}