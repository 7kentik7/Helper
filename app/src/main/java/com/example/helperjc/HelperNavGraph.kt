package com.example.helperjc

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.helperjc.presentationJC.addEditPlan.AddEditPlanDialog
import com.example.helperjc.presentationJC.helper.HelperScreen

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
                onPlanItemClick = {},
                onAddPlanClick = { navActions.navigateToAddEditPlan() },
                onTasksClick = {},
                onNotesClick = {},
                onAddTaskForPlanClick = {}
            )
        }
        composable(route = Screen.AddEditPlan.route) {
            AddEditPlanDialog(
                onDismissClick = { navActions.navigateUp() },
                onSaveButtonClick = { navActions.navigateUp() }
            )
        }


    }
}