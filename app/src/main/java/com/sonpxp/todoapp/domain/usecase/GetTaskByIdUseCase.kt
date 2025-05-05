package com.sonpxp.todoapp.domain.usecase

import com.sonpxp.todoapp.domain.model.Task
import com.sonpxp.todoapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(id: Long): Flow<Task?> {
        return taskRepository.getTaskById(id)
    }
}