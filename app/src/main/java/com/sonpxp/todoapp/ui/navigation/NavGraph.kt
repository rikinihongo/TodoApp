package com.sonpxp.todoapp.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sonpxp.todoapp.ui.features.taskdetail.TaskListScreen
import com.sonpxp.todoapp.ui.features.tasklist.TaskDetailScreen

/**
 * Route là object @Serializable, loại bỏ hoàn toàn chuỗi route.
 * Type-safe 100% nhờ Kotlin Serialization và composable<T>, không cần navArgument hay NavType cho tham số.
 * Tích hợp tham số trực tiếp vào data class @Serializable, đơn giản hóa việc truyền dữ liệu phức tạp.
 *
 * Type-safe tuyệt đối:
 * Route và tham số được định nghĩa bằng @Serializable, loại bỏ lỗi cú pháp liên quan đến chuỗi route.
 * Compiler kiểm tra type-safety tại compile-time, giảm lỗi runtime.
 *
 * Đơn giản hóa tham số:
 * Ví dụ: Thay vì task_detail?taskId={taskId}, bạn có thể dùng @Serializable data class TaskDetail(val taskId: Long)
 *
 * Code ngắn gọn hơn:
 * Loại bỏ boilerplate code như navArgument, NavType, và chuỗi route dài dòng.
 * Điều hướng chỉ cần navController.navigate(route = Object) thay vì xây dựng chuỗi thủ công.
 *
 * Tương lai-proof:
 * Đây là hướng đi mới nhất của Jetpack Navigation, được Android khuyến nghị (tính đến tháng 5/2025),
 * đảm bảo hỗ trợ lâu dài và tích hợp tốt với các thư viện Compose khác.
 *
 * @link: https://developer.android.com/develop/ui/compose/navigation
 * */
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TaskList,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<TaskList>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(400)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(400)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(400)
                )
            }
        ) {
            TaskListScreen(
                onTaskClick = { taskId ->
                    navController.navigate(TaskDetail(taskId = taskId))
                },
                onAddTaskClick = {
                    navController.navigate(TaskDetail())
                }
            )
        }
        composable<TaskDetail>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(400)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(400)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(400)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(400)
                )
            }
        ) { backStackEntry ->
            val taskDetail = backStackEntry.toRoute<TaskDetail>()
            TaskDetailScreen(
                taskId = taskDetail.taskId,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
