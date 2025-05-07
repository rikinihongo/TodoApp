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

/**
 * Phiên bản v1 sử dụng Sealed class cung cấp type-safety ở mức route, nhưng vẫn dựa vào chuỗi nên có nguy cơ lỗi cú pháp.
 * Yêu cầu khai báo NavType và navArgument cho tham số, hơi dài dòng.
 * Sealed class: Sử dụng Screen để định nghĩa route, kết hợp với createRoute để xây dựng route type-safe.
 *
 * Ưu điểm:
 * Sealed class giúp quản lý route tập trung, dễ mở rộng khi thêm màn hình mới.
 * Hàm createRoute cung cấp cách xây dựng route type-safe, giảm nguy cơ lỗi cú pháp khi truyền tham số.
 * Tách biệt logic route khỏi composable, giúp code sạch hơn.
 *
 * Nhược điểm:
 * Cách định nghĩa route (task_detail?taskId=$taskId) vẫn phụ thuộc vào string concatenation,
 * có thể không hoàn toàn type-safe nếu mở rộng với nhiều tham số phức tạp.
 *
 * */
@Composable
fun NavGraphV1(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(
            route = Screen.TaskList.route
        ) {
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
                    //navController.popBackStack()
                    navController.navigateUp()
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object TaskDetail : Screen("task_detail") {
        fun createRoute(taskId: Long): String {
            return "$route?taskId=$taskId"
        }
    }
}