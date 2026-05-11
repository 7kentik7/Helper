package com.example.helperjc.presentationJC.addEditPlan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.enums.PlanRepeatType
import io.mhssn.colorpicker.ColorPickerDialog
import io.mhssn.colorpicker.ColorPickerType

@Composable
fun AddEditPlanScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPlanViewModel = hiltViewModel(),
    onArrowBackClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.errorEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AddEditPlanAppBar(
                onSavePlanClick = { if (viewModel.onSavePlanClick()) onSaveButtonClick() },
                onArrowBackClick = onArrowBackClick,
                onColorPeekerClick = { showColorPicker = true },
                planColor = state.color
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
                isError = state.title.isBlank() && state.showTitleError,
                supportingText = {
                    if (state.title.isBlank() && state.showTitleError) {
                        Text(
                            text = stringResource(R.string.title_cannot_be_empty),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 4.dp, end = 4.dp))
            )
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                onClick = { showDatePicker = true }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = state.endTime ?: "Выбрать дату",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            RepeatTypePicker(
                selected = state.repeatType,
                onSelected = viewModel::onRepeatTypeChange,
                enabled = state.endTime != null
            )
            AnimatedVisibility(visible = state.endTime != null) {
                InputChip(
                    selected = false,
                    onClick = {},
                    label = { Text(state.endTime ?: "") },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { viewModel.onEndTimeDelete() }
                        )
                    }
                )
            }
        }

        ColorPeeker(
            showColorDialog = showColorPicker,
            onPlanColorChange = viewModel::onColorChanged,
            onDismiss = { showColorPicker = !showColorPicker }
        )

        if (showDatePicker) {
            PlanDatePicker(
                onDateSelected = { millis -> viewModel.onEndTimeChange(millis) },
                onDismiss = { showDatePicker = !showDatePicker }
            )
        }
    }
}

@Composable
private fun RepeatTypePicker(
    selected: PlanRepeatType,
    onSelected: (PlanRepeatType) -> Unit,
    enabled: Boolean
) {
    val options = listOf(
        PlanRepeatType.NONE to "Без напоминаний",
        PlanRepeatType.DAILY to "Каждый день",
        PlanRepeatType.WEAKLY to "Каждую неделю",
        PlanRepeatType.MONTHLY to "Каждый месяц",
        PlanRepeatType.YEARLY to "Каждый год"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
    ) {
        Text(
            text = "Напоминать:",
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) MaterialTheme.colorScheme.onBackground
            else MaterialTheme.colorScheme.outline
        )
        options.forEach { (type, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected == type,
                        enabled = enabled,
                        onClick = { onSelected(type) },
                        role = Role.RadioButton
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selected == type,
                    onClick = null,
                    enabled = enabled
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    color = if (enabled) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditPlanAppBar(
    onSavePlanClick: () -> Unit,
    onArrowBackClick: () -> Unit,
    onColorPeekerClick: () -> Unit,
    planColor: Color
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            IconButton(onClick = onColorPeekerClick) {
                Icon(
                    painter = painterResource(R.drawable.color_peeker_icon),
                    contentDescription = null,
                    tint = planColor
                )
            }
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
            IconButton(onClick = onArrowBackClick) {
                Icon(
                    ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null
                )
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ColorPeeker(
    showColorDialog: Boolean,
    onPlanColorChange: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    ColorPickerDialog(
        show = showColorDialog,
        type = ColorPickerType.Ring(
            ringWidth = 10.dp,
            previewRadius = 80.dp,
            showAlphaBar = false,
            showColorPreview = true,
            showLightnessBar = false,
            showDarknessBar = true
        ),
        properties = DialogProperties(),
        onDismissRequest = onDismiss,
        onPickedColor = {
            onPlanColorChange(it)
            onDismiss()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDatePicker(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}