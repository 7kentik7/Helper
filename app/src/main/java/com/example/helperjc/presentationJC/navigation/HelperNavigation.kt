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
    data object PlanDetailsScreen : Screen("planDetailsScreen?planId={planId}") {
        fun createRoute(planId: Int? = null): String {
            return if (planId != null) {
                "planDetailsScreen?planId=$planId"
            } else {
                "planDetailsScreen"
            }
        }
    }

    data object AddEditTask : Screen("addEditTask?taskId={taskId}&planId={planId}") {
        fun createRoute(
            taskId: Int? = null,
            planId: Int? = null
        ): String {
            val params = mutableListOf<String>()

            taskId?.let { params.add("taskId=$it") }
            planId?.let { params.add("planId=$it") }

            return if (params.isEmpty()) {
                "addEditTask"
            } else {
                "addEditTask?${params.joinToString("&")}"
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

    fun navigateToAddEditTask(taskId: Int? = null, planId: Int? = null) {
        navController.navigate(Screen.AddEditTask.createRoute(taskId, planId)) {
            launchSingleTop = true
        }
    }

    fun navigateToTasksScreen() {
        navController.navigate(Screen.TasksList.route) {
            launchSingleTop = true
        }
    }
    fun navigateToPlanDetailsScreen(planId: Int? = null) {
        navController.navigate(Screen.PlanDetailsScreen.createRoute(planId)) {
            launchSingleTop = true
        }
    }

}