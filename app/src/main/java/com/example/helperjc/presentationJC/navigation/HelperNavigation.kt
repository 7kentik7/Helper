package com.example.helperjc.presentationJC.navigation

import androidx.navigation.NavController

sealed class Screen(val route: String) {
    data object Helper : Screen("helper")
    data object AddEditPlan : Screen("addEditPlan?planId={planId}") {
        fun createRoute(planId: Int? = null): String {
            return if (planId != null) {
                "addEditPlan?planId=$planId"
            } else {
                "addEditPlan"
            }
        }
    }

    data object AddEditTask : Screen("addEditTask?taskId={taskId}") {
        fun createRoute(taskId: Int? = null): String {
            return if (taskId != null) {
                "addEditTask?taskId=$taskId"
            } else {
                "addEditTask"
            }
        }
    }

    data object TasksList : Screen("tasksList")
}

class HelperNavigationActions(
    private val navController: NavController
) {
    fun navigateUp() {
        navController.popBackStack()
    }

    fun navigateToAddEditPlan(planId: Int? = null) {
        navController.navigate(Screen.AddEditPlan.createRoute(planId)) {
            launchSingleTop = true
        }
    }

    fun navigateToAddEditTask(taskId: Int?=null) {
        navController.navigate(Screen.AddEditTask.createRoute(taskId)) {
            launchSingleTop = true
        }
    }

    fun navigateToTasksScreen() {
        navController.navigate(Screen.TasksList.route) {
            launchSingleTop = true
        }
    }

}