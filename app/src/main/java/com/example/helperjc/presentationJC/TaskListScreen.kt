package com.example.helperjc.presentationJC

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helperjc.R
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentationJC.ui.theme.HelperJCTheme
import com.example.helperjc.presentationJC.ui.theme.PriorityHigh
import com.example.helperjc.presentationJC.ui.theme.PriorityLow
import com.example.helperjc.presentationJC.ui.theme.PriorityMedium

@Composable
fun TaskListScreen() {
    Scaffold(
        topBar = { AppBar() },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->
        TaskItem(
            innerPadding = innerPadding,
            task = Task(
                title = "Задача",
                description = "Описание",
                isActive = true,
                priority = TaskPriority.LOW
            )
        )
    }
}

@Composable
fun TaskItem(innerPadding: PaddingValues, task: Task) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val color = when (task.priority) {
                TaskPriority.LOW -> PriorityLow
                TaskPriority.MEDIUM -> PriorityMedium
                TaskPriority.HIGH -> PriorityHigh
            }

            RadioButton(
                selected = task.isActive,
                onClick = { !task.isActive },
                colors = RadioButtonDefaults.colors(
                    selectedColor = color,
                    unselectedColor = MaterialTheme.colorScheme.outline
                )
            )

            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = task.title, fontSize = 18.sp)
                if (!task.description.isNullOrBlank()) {
                    Text(text = task.description, fontSize = 16.sp)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = { Text(text = stringResource(R.string.tasks), fontSize = 24.sp) },
        navigationIcon = {
            IconButton(onClick = {}) {
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


@Preview
@Composable
fun PreviewTaskListScreenDark() {
    HelperJCTheme(darkTheme = true, dynamicColor = false) {
        TaskListScreen()
    }
}

@Preview
@Composable
fun PreviewTaskListScreenLight() {
    HelperJCTheme(darkTheme = false, dynamicColor = false) {
        TaskListScreen()
    }
}