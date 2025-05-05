package com.sonpxp.todoapp.ui.navigation

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object TaskDetail : Screen("task_detail") {
        fun createRoute(taskId: Long): String {
            return "$route?taskId=$taskId"
        }
    }
}