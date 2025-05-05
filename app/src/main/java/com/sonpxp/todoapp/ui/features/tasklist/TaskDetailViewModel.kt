package com.sonpxp.todoapp.ui.features.tasklist


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonpxp.todoapp.domain.model.Priority
import com.sonpxp.todoapp.domain.model.Task
import com.sonpxp.todoapp.domain.usecase.AddTaskUseCase
import com.sonpxp.todoapp.domain.usecase.GetTaskByIdUseCase
import com.sonpxp.todoapp.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state

    init {
        savedStateHandle.get<Long>("taskId")?.let { taskId ->
            if (taskId != -1L) {
                getTask(taskId)
            }
        }
    }

    private fun getTask(id: Long) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getTaskByIdUseCase(id)
                .catch { error ->
                    _state.update {
                        it.copy(
                            error = error.localizedMessage,
                            isLoading = false
                        )
                    }
                }
                .collect { task ->
                    task?.let {
                        _state.update {
                            it.copy(
                                task = task,
                                title = task.title,
                                description = task.description,
                                isCompleted = task.isCompleted,
                                dueDate = task.dueDate,
                                priority = task.priority,
                                isLoading = false
                            )
                        }
                    }
                }
        }
    }

    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        _state.update { it.copy(description = description) }
    }

    fun onCompletedChange(isCompleted: Boolean) {
        _state.update { it.copy(isCompleted = isCompleted) }
    }

    fun onDueDateChange(dueDate: Date?) {
        _state.update { it.copy(dueDate = dueDate) }
    }

    fun onPriorityChange(priority: Priority) {
        _state.update { it.copy(priority = priority) }
    }

    fun saveTask() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            try {
                val taskToSave = Task(
                    id = state.value.task?.id ?: 0,
                    title = state.value.title,
                    description = state.value.description,
                    isCompleted = state.value.isCompleted,
                    createdDate = state.value.task?.createdDate ?: Date(),
                    dueDate = state.value.dueDate,
                    priority = state.value.priority
                )

                if (taskToSave.id == 0L) {
                    addTaskUseCase(taskToSave)
                } else {
                    updateTaskUseCase(taskToSave)
                }

                _state.update { it.copy(isSaving = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.localizedMessage,
                        isSaving = false
                    )
                }
            }
        }
    }
}