package com.sonpxp.todoapp.data.mapper

import com.sonpxp.todoapp.data.local.entity.TaskEntity
import com.sonpxp.todoapp.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdDate = createdDate,
        dueDate = dueDate,
        priority = priority
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        createdDate = createdDate,
        dueDate = dueDate,
        priority = priority
    )
}