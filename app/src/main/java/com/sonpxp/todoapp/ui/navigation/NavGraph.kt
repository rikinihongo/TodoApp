package com.sonpxp.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sonpxp.todoapp.ui.features.taskdetail.TaskListScreen
import com.sonpxp.todoapp.ui.features.tasklist.TaskDetailScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(
                onTaskClick = { taskId ->
                    navController.navigate(Screen.TaskDetail.createRoute(taskId))
                },
                onAddTaskClick = {
                    navController.navigate(Screen.TaskDetail.route)
                }
            )
        }
        composable(
            route = Screen.TaskDetail.route + "?taskId={taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailScreen(
                taskId = taskId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}