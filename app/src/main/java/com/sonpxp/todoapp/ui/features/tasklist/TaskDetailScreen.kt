package com.sonpxp.todoapp.ui.features.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sonpxp.todoapp.domain.model.Priority
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: Long,
    onNavigateBack: () -> Unit,
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var enabled by remember { mutableStateOf(false) }

    val isNewTask = taskId == -1L
    val title = if (isNewTask) "New Task" else "Edit Task"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            enabled = state.title.isNotBlank() && !state.isSaving
            FloatingActionButton(
                onClick = {
                    if (enabled) {
                        viewModel.saveTask()
                        onNavigateBack()
                    }
                },
                //enabled = state.title.isNotBlank() && !state.isSaving
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Task")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.error != null && !isNewTask) {
                Text(
                    text = state.error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                TaskDetailContent(
                    title = state.title,
                    description = state.description,
                    isCompleted = state.isCompleted,
                    dueDate = state.dueDate,
                    priority = state.priority,
                    onTitleChange = viewModel::onTitleChange,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    onCompletedChange = viewModel::onCompletedChange,
                    onDueDateChange = viewModel::onDueDateChange,
                    onPriorityChange = viewModel::onPriorityChange
                )
            }

            if (state.isSaving) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun TaskDetailContent(
    title: String,
    description: String,
    isCompleted: Boolean,
    dueDate: Date?,
    priority: Priority,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCompletedChange: (Boolean) -> Unit,
    onDueDateChange: (Date?) -> Unit,
    onPriorityChange: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = onCompletedChange
            )
            Text("Completed")
        }

        PrioritySelector(
            selectedPriority = priority,
            onPrioritySelected = onPriorityChange
        )

        DueDateSelector(
            dueDate = dueDate,
            onDueDateSelected = onDueDateChange
        )
    }
}

@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedPriority.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Priority.values().forEach { priority ->
                    DropdownMenuItem(
                        text = { Text(priority.name) },
                        onClick = {
                            onPrioritySelected(priority)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DueDateSelector(
    dueDate: Date?,
    onDueDateSelected: (Date?) -> Unit
) {
    var showDateOptions by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Due Date",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDateOptions = true }
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dueDate?.let {
                    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
                } ?: "No due date",
                style = MaterialTheme.typography.bodyLarge
            )

            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Select Due Date"
            )
        }

        if (showDateOptions) {
            DateOptions(
                dueDate = dueDate,
                onDueDateSelected = {
                    onDueDateSelected(it)
                    showDateOptions = false
                },
                onDismiss = { showDateOptions = false }
            )
        }
    }
}

@Composable
fun DateOptions(
    dueDate: Date?,
    onDueDateSelected: (Date?) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Today
        Button(
            onClick = {
                onDueDateSelected(Date())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Today")
        }

        // Tomorrow
        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                onDueDateSelected(calendar.time)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomorrow")
        }

        // Next Week
        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                onDueDateSelected(calendar.time)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next Week")
        }

        // No Due Date
        Button(
            onClick = {
                onDueDateSelected(null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("No Due Date")
        }

        // Cancel
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}
