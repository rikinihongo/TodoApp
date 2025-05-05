package com.sonpxp.todoapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sonpxp.todoapp.domain.model.Task

@Composable
fun SimpleTaskItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (task.isCompleted)
                Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
            contentDescription = "Task Status",
            tint = if (task.isCompleted)
                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = task.title,
            textDecoration = if (task.isCompleted)
                TextDecoration.LineThrough else TextDecoration.None
        )
    }
}