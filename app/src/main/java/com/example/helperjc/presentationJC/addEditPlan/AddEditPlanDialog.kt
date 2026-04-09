package com.example.helperjc.presentationJC.addEditPlan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.helperjc.R
import com.example.helperjc.presentationJC.ui.theme.HelperJCTheme


@Composable
fun AddEditPlanDialog(
    modifier: Modifier = Modifier,
    viewModel: AddPlanViewModel = hiltViewModel(),
    onDismissClick: () -> Unit,
    onSaveButtonClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.add_plan),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = state.title,
                    onValueChange = viewModel::onTitleChange,
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PaddingValues(start = 4.dp, end = 4.dp))
                )
                val chipState = remember { mutableStateOf(false) }

                InputChip(
                    onClick = {
                        chipState.value = !chipState.value
                    },
                    label = { },
                    selected = chipState.value,
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(4.dp)

                    ) {
                        Text(
                            text = stringResource(R.string.cancel),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            modifier = Modifier.clickable(onClick = { if (viewModel.onSaveButtonClick()) onSaveButtonClick() }),
                            text = stringResource(R.string.save),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }
            }
        }
    }
}


//@Preview
//@Composable
//fun PreviewAddEditPlanDialogDark() {
//    HelperJCTheme(darkTheme = true, dynamicColor = false) {
//        AddEditPlanDialog()
//    }
//}
//
//@Preview
//@Composable
//fun PreviewAddEditPlanDialogLight() {
//    HelperJCTheme(darkTheme = false, dynamicColor = false) {
//        AddEditPlanDialog()
//    }
//}