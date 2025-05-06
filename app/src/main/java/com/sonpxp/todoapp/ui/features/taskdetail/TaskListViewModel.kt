package com.sonpxp.todoapp.ui.features.taskdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sonpxp.todoapp.domain.model.Task
import com.sonpxp.todoapp.domain.usecase.DeleteTaskUseCase
import com.sonpxp.todoapp.domain.usecase.GetTasksUseCase
import com.sonpxp.todoapp.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TaskListState())
    val state: StateFlow<TaskListState> = _state

    init {
        getTasks()
    }

    private fun getTasks() {
        _state.update { it.copy(isLoading = true) }
        getTasksUseCase()
            .onEach { tasks ->
                delay(2000)
                _state.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false
                    )
                }
            }
            .catch { error ->
                _state.update {
                    it.copy(
                        error = error.localizedMessage,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = !task.isCompleted)
            updateTaskUseCase(updatedTask)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }
}