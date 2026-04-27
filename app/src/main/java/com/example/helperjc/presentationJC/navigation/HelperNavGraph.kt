package com.example.helperjc.presentationJC.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.helperjc.presentationJC.addEditPlan.AddEditPlanScreen
import com.example.helperjc.presentationJC.addEditTask.AddEditTaskDialog
import com.example.helperjc.presentationJC.helper.HelperScreen
import com.example.helperjc.presentationJC.todolist.TaskListScreen

@Composable
fun HelperNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Helper.route,
    navActions: HelperNavigationActions = remember(navController) {
        HelperNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Helper.route) {
            HelperScreen(
                modifier = Modifier.fillMaxSize(),
                onPlanItemClick = { id -> navActions.navigateToAddEditPlan(id) },
                onAddPlanClick = { navActions.navigateToAddEditPlan() },
                onTasksClick = { navActions.navigateToTasksScreen() },
                onNotesClick = {},
                onAddTaskForPlanClick = {}
            )
        }
        composable(
            route = Screen.AddEditPlan.route,
            arguments = listOf(
                navArgument("planId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {

            AddEditPlanScreen(
                modifier = Modifier.fillMaxSize(),
                onArrowBackClick = { navActions.navigateUp() },
                onSaveButtonClick = { navActions.navigateUp() }
            )
        }
        composable(route = Screen.TasksList.route) {
            TaskListScreen(
                modifier = Modifier.fillMaxSize(),
                onBackArrowClick = { navActions.navigateUp() },
                onAddTaskClick = { navActions.navigateToAddEditTask() },
                onTaskClick = { id -> navActions.navigateToAddEditTask(id) }
            )
        }
        composable(route = Screen.AddEditTask.route) {
            AddEditTaskDialog(
                modifier = Modifier.fillMaxSize(),
                onDismissClick = { navActions.navigateUp() },
                onSaveButtonClick = { navActions.navigateUp() }
            )
        }
    }
}