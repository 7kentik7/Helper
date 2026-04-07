package com.example.helperjc.presentationJC

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog() {
    ModalBottomSheet(onDismissRequest = { }) {
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
            TextFields(descriptionState)
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
                    RadioButtonsPriority(task = Task(priority = TaskPriority.HIGH))
                }

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.save),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TextFields(descriptionState: State<Boolean>) {
    TextField(
        value = "",
        onValueChange = { },
        label = { Text(stringResource(R.string.name)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(start = 4.dp, end = 4.dp)),
    )

    if (descriptionState.value) {
        TextField(
            value = "",
            onValueChange = { },
            label = { Text(text = stringResource(R.string.description)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(start = 4.dp, end = 4.dp)),
        )
    }
}

@Composable
private fun RadioButtonsPriority(task: Task?) {
    val radioOptions = listOf(
        TaskPriority.LOW,
        TaskPriority.MEDIUM,
        TaskPriority.HIGH
    )
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(task?.priority ?: TaskPriority.MEDIUM)
    }
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        radioOptions.forEach { priority ->

            Row(
                modifier = Modifier
                    .selectable(
                        selected = (priority == selectedOption),
                        onClick = { onOptionSelected(priority) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val radioButtonColorByPriority = when (priority) {
                    TaskPriority.LOW -> PriorityLow
                    TaskPriority.MEDIUM -> PriorityMedium
                    TaskPriority.HIGH -> PriorityHigh
                }
                RadioButton(
                    modifier = Modifier.padding(5.dp),
                    selected = priority == selectedOption,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        unselectedColor = radioButtonColorByPriority,
                        selectedColor = radioButtonColorByPriority
                    )
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewAddEditTaskDialogDark() {
    HelperJCTheme(darkTheme = true, dynamicColor = false) {
        AddEditTaskDialog()
    }
}

@Preview
@Composable
fun PreviewAddEditTaskDialogLight() {
    HelperJCTheme(darkTheme = false, dynamicColor = false) {
        AddEditTaskDialog()
    }
}