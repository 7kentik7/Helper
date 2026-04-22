package com.example.helperjc.presentationJC.helper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.parseToString
import com.example.helperjc.presentationJC.theme.DeleteColor
import com.example.helperjc.presentationJC.theme.ProgressFifthStep
import com.example.helperjc.presentationJC.theme.ProgressFirstStep
import com.example.helperjc.presentationJC.theme.ProgressFourthStep
import com.example.helperjc.presentationJC.theme.ProgressSecondStep
import com.example.helperjc.presentationJC.theme.ProgressThirdStep


@Composable
fun HelperScreen(
    modifier: Modifier = Modifier,
    viewModel: HelperViewModel = hiltViewModel(),
    onTasksClick: () -> Unit,
    onNotesClick: () -> Unit,
    onPlanItemClick: (PlanDetails) -> Unit,
    onAddPlanClick: () -> Unit,
    onAddTaskForPlanClick: (PlanDetails) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            AppBar(
                onTasksClick = onTasksClick,
                onNotesClick = onTasksClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPlanClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->
        if (state.planDetailsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.emptyList),
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 20.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.planDetailsList, key = { it.plan.id }) { planDetails ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deletePlan(planDetails)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        modifier = Modifier.animateItem(),
                        state = dismissState,
                        backgroundContent = {
                            DeleteBackground()
                        }
                    ) {
                        PlanItem(
                            planDetails = planDetails,
                            onPlanItemClick = onPlanItemClick,
                            onAddTaskForPlanClick = onAddTaskForPlanClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(DeleteColor)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    onTasksClick: () -> Unit,
    onNotesClick: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            IconButton(onClick = onNotesClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.notes_icon),
                    contentDescription = null
                )
            }
            IconButton(onClick = onTasksClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.tasks_icon),
                    contentDescription = null
                )
            }
        },
        title = {},
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }
    )
}


@Composable
private fun PlanItem(
    innerPadding: PaddingValues = PaddingValues(),
    planDetails: PlanDetails,
    onPlanItemClick: (PlanDetails) -> Unit,
    onAddTaskForPlanClick: (PlanDetails) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = Color.Cyan),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { onPlanItemClick(planDetails) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            var endTimeString by remember(key1 = planDetails.plan.endTime) {
                mutableStateOf(planDetails.plan.endTime?.parseToString())
            }
            endTimeString?.let {
                Text(text = it, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
            }
            Text(text = planDetails.plan.title, fontSize = 18.sp)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    ProgressBarForItemHelper(planDetails.progress)
                    Text(
                        text = stringResource(
                            id = R.string.tasks_count,
                            planDetails.countOfTasks
                        ),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = stringResource(
                            id = R.string.completed_tasks_count,
                            planDetails.countOfCompletedTasks
                        ),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                IconButton(onClick = { onAddTaskForPlanClick(planDetails) }) {
                    Icon(
                        painter = painterResource(R.drawable.add_task),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressBarForItemHelper(progress: Int) {
    val progressFloat = progress / 100f
    val color = when (progress) {
        in 0 until 20 -> {
            ProgressFirstStep
        }

        in 20 until 40 -> {
            ProgressSecondStep
        }

        in 40 until 60 -> {
            ProgressThirdStep
        }

        in 60 until 80 -> {
            ProgressFourthStep
        }

        in 80..100 -> {
            ProgressFifthStep
        }

        else -> {
            ProgressFirstStep
        }

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressFloat)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}