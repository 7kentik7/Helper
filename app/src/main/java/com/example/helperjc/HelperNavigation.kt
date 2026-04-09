package com.example.helperjc

import androidx.navigation.NavController

sealed class Screen(val route: String) {
    data object Helper : Screen("helper")

    data object AddEditPlan : Screen("addEditPlan")

    data object TasksList : Screen("tasksList")
}

class HelperNavigationActions(
    private val navController: NavController
) {
    fun navigateUp() {
        navController.popBackStack()
    }

    fun navigateToAddEditPlan() {
        navController.navigate(Screen.AddEditPlan.route) {
            launchSingleTop = true
        }
    }

}