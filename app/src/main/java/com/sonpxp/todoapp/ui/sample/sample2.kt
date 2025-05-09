package com.sonpxp.todoapp.ui.sample

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.w3c.dom.Text

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen() {
    // State
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mới nhất", "Xu hướng", "Đã lưu")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ứng dụng Tin tức") },
                actions = {
                    IconButton(onClick = { /* xử lý */ }) {
                        Icon(Icons.Default.Search, "Tìm kiếm")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation {
                tabs.forEachIndexed { index, title ->
                    BottomNavigationItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            when (index) {
                                0 -> Icon(Icons.Default.Home, null)
                                1 -> Icon(Icons.Default.Favorite, null)
                                2 -> Icon(Icons.Default.Bookmark, null)
                            }
                        },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Nội dung chính
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize()
        ) {
            items(10) { index ->
                NewsItemCard(index)
            }
        }
    }
}

fun BottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (Icon) -> Unit,
    label: @Composable (Text) -> Unit
) {
    TODO("Not yet implemented")
}

@Composable
fun BottomNavigation(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun NewsItemCard(index: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tiêu đề tin tức $index",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nội dung mô tả ngắn về tin tức. Đây là tin tức số $index trong danh sách.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "1 giờ trước",
                    style = MaterialTheme.typography.bodySmall,
                )

                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Lưu tin",
                    modifier = Modifier.clickable { /* xử lý */ }
                )
            }
        }
    }
}