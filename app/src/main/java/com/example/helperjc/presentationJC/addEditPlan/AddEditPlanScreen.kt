package com.example.helperjc.presentationJC.addEditPlan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.parseToString


@Composable
fun AddEditPlanScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPlanViewModel = hiltViewModel(),
    onArrowBackClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                onSavePlanClick = { if (viewModel.onSavePlanClick()) onSaveButtonClick() },
                onArrowBackClick = onArrowBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text(stringResource(R.string.name)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 4.dp, end = 4.dp))
            )
            var chipState by remember { mutableStateOf(false) }

            InputChip(
                onClick = {
                    chipState = !chipState
                },
                label = { state.endTime },
                selected = chipState,
                trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        Modifier
                            .size(16.dp)
                    )
                }
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_more_time_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    onSavePlanClick: () -> Unit,
    onArrowBackClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            IconButton(onClick = onSavePlanClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.check_icon),
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource(R.string.add_plan),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = { onArrowBackClick() }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null
                )
            }
        }
    )
}