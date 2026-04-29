package com.example.helperjc.presentationJC.addEditPlan

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.domain.plans.Plan
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
    Scaffold(
        modifier = modifier, topBar = {
            AppBar(
                onSavePlanClick = { if (viewModel.onSavePlanClick()) onSaveButtonClick() },
                onArrowBackClick = onArrowBackClick,
                onColorPeekerClick = { showColorPicker = true }
            )
        }) { paddingValues ->
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
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                onClick = { showDatePicker = true }) {
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
            AnimatedVisibility(visible = state.endTime != null) {
                InputChip(selected = false, onClick = {}, label = {
                    Text(state.endTime ?: "")
                }, trailingIcon = {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.onEndTimeDelete()
                        })
                })
            }
        }
        ColorPeeker(
            showColorDialog = showColorPicker,
            onPlanColorChange = viewModel::onColorChanged,
            onDismiss = { showColorPicker = !showColorPicker }
        )

        if (showDatePicker) {
            PlanDatePicker(onDateSelected = { millis ->
                viewModel.onEndTimeChange(millis)
            }, onDismiss = { showDatePicker = !showDatePicker })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    onSavePlanClick: () -> Unit,
    onArrowBackClick: () -> Unit,
    onColorPeekerClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ), actions = {
            IconButton(onClick = onColorPeekerClick) {
                Icon(
                    painter = painterResource(R.drawable.baseline_more_time_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onSavePlanClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.check_icon),
                    contentDescription = null
                )
            }
        }, title = {
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource(R.string.add_plan),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }, navigationIcon = {
            IconButton(onClick = { onArrowBackClick() }) {
                Icon(
                    ImageVector.vectorResource(R.drawable.outline_arrow_back_24),
                    contentDescription = null
                )
            }
        })
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

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(
            onClick = {
                datePickerState.selectedDateMillis?.let {
                    onDateSelected(it)
                }
                onDismiss()
            }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Отмена")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}