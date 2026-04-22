package com.example.helperjc.presentationJC.addEditTask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentationJC.theme.PriorityHigh
import com.example.helperjc.presentationJC.theme.PriorityLow
import com.example.helperjc.presentationJC.theme.PriorityMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog(
    modifier: Modifier = Modifier,
    viewmodel: AddTaskViewModel = hiltViewModel(),
    onDismissClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val state by viewmodel.state.collectAsStateWithLifecycle()
    ModalBottomSheet(onDismissRequest = onDismissClick) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            val descriptionState = remember { mutableStateOf(false) }
            TextField(
                value = state.title,
                onValueChange = viewmodel::onTitleChange,
                label = { Text(stringResource(R.string.name)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 4.dp, end = 4.dp)),
            )

            if (descriptionState.value) {
                TextField(
                    value = state.description,
                    onValueChange = viewmodel::onDescriptionChange,
                    label = { Text(text = stringResource(R.string.description)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(start = 4.dp, end = 4.dp)),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (!descriptionState.value) {
                            descriptionState.value = !descriptionState.value
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.description_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                    RadioButtonsPriority(
                        selected = state.priority,
                        onSelected = viewmodel::onPriorityChange
                    )
                }
                Text(
                    modifier = Modifier
                        .clickable(onClick = { if (viewmodel.onSaveButtonClick()) onSaveButtonClick() })
                        .padding(8.dp),
                    text = stringResource(R.string.save),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )

            }
        }
    }
}


@Composable
private fun RadioButtonsPriority(
    selected: TaskPriority,
    onSelected: (TaskPriority) -> Unit
) {
    val radioOptions = listOf(
        TaskPriority.LOW,
        TaskPriority.MEDIUM,
        TaskPriority.HIGH
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        radioOptions.forEach { priority ->
            Row(
                modifier = Modifier
                    .selectable(
                        selected = (priority == selected),
                        onClick = { onSelected(priority) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val color = when (priority) {
                    TaskPriority.LOW -> PriorityLow
                    TaskPriority.MEDIUM -> PriorityMedium
                    TaskPriority.HIGH -> PriorityHigh
                }

                RadioButton(
                    selected = priority == selected,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = color,
                        unselectedColor = color
                    )
                )
            }
        }
    }
}