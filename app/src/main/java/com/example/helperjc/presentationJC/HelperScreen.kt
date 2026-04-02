package com.example.helperjc.presentationJC

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helperjc.R
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.parseToString
import com.example.helperjc.presentationJC.ui.theme.HelperJCTheme
import com.example.helperjc.presentationJC.ui.theme.ProgressFifthStep
import com.example.helperjc.presentationJC.ui.theme.ProgressFirstStep
import com.example.helperjc.presentationJC.ui.theme.ProgressFourthStep
import com.example.helperjc.presentationJC.ui.theme.ProgressSecondStep
import com.example.helperjc.presentationJC.ui.theme.ProgressThirdStep
import com.example.helperjc.toLocalDateTime


@Composable
fun HelperScreen() {
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
        PlanItem(
            innerPadding = innerPadding,
            planDetails = PlanDetails(
                plan = Plan(title = "План", endTime = null),
                progress = 0,
                countOfTasks = 3,
                countOfCompletedTasks = 2,
                tasks = listOf()
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.notes_icon),
                    contentDescription = null
                )
            }
            IconButton(onClick = {}) {
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
private fun PlanItem(innerPadding: PaddingValues, planDetails: PlanDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = Color.Cyan),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {}
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            planDetails.plan.endTime?.parseToString()?.let {
                Text(text = it, fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
            }
            Text(text = planDetails.plan.title, fontSize = 18.sp)
            ProgressBarForItemHelper(planDetails.progress)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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

                IconButton(onClick = {}) {
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


@Preview
@Composable
fun PreviewHelperScreenDark() {
    HelperJCTheme(darkTheme = true, dynamicColor = false) {
        HelperScreen()
    }
}

@Preview
@Composable
fun PreviewHelperScreenLight() {
    HelperJCTheme(darkTheme = false, dynamicColor = false) {
        HelperScreen()
    }
}