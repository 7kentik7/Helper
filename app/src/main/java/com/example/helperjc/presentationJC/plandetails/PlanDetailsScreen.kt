package com.example.helperjc.presentationJC.plandetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentationJC.theme.DeleteColor
import com.example.helperjc.presentationJC.theme.PriorityHigh
import com.example.helperjc.presentationJC.theme.PriorityLow
import com.example.helperjc.presentationJC.theme.PriorityMedium
import com.example.helperjc.utils.muted

@Composable
fun PlanDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: PlanDetailsViewModel = hiltViewModel(),
    onBackArrowClick: () -> Unit,
    onAddTaskClick: (Int) -> Unit,
    onTaskClick: (Int, Int?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { AppBar(onBackArrowClick) },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTaskClick(state.planId) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->

        if (state.currencyList.isEmpty()) {
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
                    .padding(horizontal = 10.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.currencyList, key = { it.id }) { task ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTask(task)
                                true
                            } else false
                        }
                    )
                    SwipeToDismissBox(
                        modifier = Modifier.animateItem(),
                        state = dismissState,
                        backgroundContent = {
                            DeleteBackground(task)
                        }
                    ) {
                        TaskItem(
                            taskItem = task,
                            changeEnabledState = { viewModel.changeEnableState(task) },
                            onTaskClick = onTaskClick,
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun DeleteBackground(taskItem: Task) {
    val isCompleted = taskItem.isCompleted
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(if (!isCompleted) DeleteColor else DeleteColor.muted())
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

@Composable
fun TaskItem(
    taskItem: Task,
    changeEnabledState: () -> Unit,
    onTaskClick: (Int, Int?) -> Unit
) {
    val colorByPriority = when (taskItem.priority) {
        TaskPriority.LOW -> PriorityLow
        TaskPriority.MEDIUM -> PriorityMedium
        TaskPriority.HIGH -> PriorityHigh
    }
    val isCompleted = taskItem.isCompleted
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor =
                if (!isCompleted)
                    MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.surface.muted()
        ),
        onClick = {
            onTaskClick(taskItem.id, taskItem.planId)
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val radioButtonColorByStatus = if (!isCompleted)
                colorByPriority
            else colorByPriority.muted()
            RadioButton(
                selected = taskItem.isCompleted,
                onClick = changeEnabledState,
                colors = RadioButtonDefaults.colors(
                    selectedColor = radioButtonColorByStatus,
                    unselectedColor = radioButtonColorByStatus
                )
            )
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = taskItem.title,
                    fontSize = 18.sp,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                )
                if (!taskItem.description.isNullOrBlank()) {
                    Text(
                        text = taskItem.description,
                        fontSize = 16.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(onBackArrowClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = { Text(text = stringResource(R.string.tasks), fontSize = 24.sp) },
        navigationIcon = {
            IconButton(onClick = onBackArrowClick) {
                Icon(
                    painter = painterResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}