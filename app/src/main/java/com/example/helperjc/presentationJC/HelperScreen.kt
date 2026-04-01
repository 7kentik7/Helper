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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helperjc.R
import com.example.helperjc.presentationJC.ui.theme.HelperJCTheme


@Composable
fun HelperScreen() {

    Scaffold(
        topBar = { AppBar() },
        containerColor = MaterialTheme.colorScheme.background

    ) { innerPadding ->
        PlanItem(
            title = "План",
            innerPadding = innerPadding,
            countOfTask = 1,
            countOfCompletedTask = 2,
            progress = 0.3f,
            deadLine = "20.02.2026"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(

) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.AccountCircle, contentDescription = null)
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
    innerPadding: PaddingValues,
    deadLine: String,
    title: String,
    progress: Float,
    countOfTask: Int,
    countOfCompletedTask: Int,

    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, color = Color.Cyan),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {}
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = deadLine, fontSize = 14.sp)
            Text(text = title, fontSize = 18.sp)
            ProgressBarForItemHelper(progress)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(
                        text = stringResource(id = R.string.tasks_count, countOfTask),
                        fontSize = 14.sp,
                    )
                    Text(
                        text = stringResource(
                            id = R.string.completed_tasks_count,
                            countOfCompletedTask
                        ),
                        fontSize = 14.sp
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Add,
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
fun ProgressBarForItemHelper(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(Color.Cyan)
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