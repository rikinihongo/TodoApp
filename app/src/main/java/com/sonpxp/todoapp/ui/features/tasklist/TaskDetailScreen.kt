package com.sonpxp.todoapp.ui.features.tasklist

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                }
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

@OptIn(ExperimentalMaterial3Api::class)
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
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedPriority.name,
                onValueChange = {},
                readOnly = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors( //, TextFieldDefaults
                    focusedTextColor = when (selectedPriority) {
                        Priority.LOW -> Color.Green
                        Priority.MEDIUM -> Color.Magenta
                        Priority.HIGH -> Color.Red
                    },
                    unfocusedTextColor = when (selectedPriority) {
                        Priority.LOW -> Color.Green
                        Priority.MEDIUM -> Color.Magenta
                        Priority.HIGH -> Color.Red
                    }
                ),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Priority.entries.forEach { priority ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = priority.name,
                                color = when (priority) {
                                    Priority.LOW -> Color.Green
                                    Priority.MEDIUM -> Color.Magenta
                                    Priority.HIGH -> Color.Red
                                }
                            )
                        },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDateSelector(
    dueDate: Date?,
    onDueDateSelected: (Date?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    dueDate?.let { calendar.time = it }

    // Prepare date options
    val dateOptions = listOf(
        "Today" to Date(),
        "Tomorrow" to Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }.time,
        "Next Week" to Calendar.getInstance().apply { add(Calendar.WEEK_OF_YEAR, 1) }.time,
        "Pick a Date" to null,
        "No Due Date" to null
    )
    val selectedDateText = dueDate?.let {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
    } ?: "No Due Date"

    // Date picker state
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = dueDate?.time ?: System.currentTimeMillis()
    )

    Column {
        Text(
            text = "Due Date",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedDateText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Select Due Date"
                    )
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dateOptions.forEach { (label, date) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            if (label == "Pick a Date") {
                                showDatePicker = true
                            } else {
                                onDueDateSelected(date)
                            }
                            expanded = false
                        }
                    )
                }
            }
        }

        // Material 3 Date Picker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onDueDateSelected(Date(millis))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState,
//                    title = { Text("Select Due Date") },
//                    headline = { Text(selectedDateText) },
                )
            }
        }
    }
}