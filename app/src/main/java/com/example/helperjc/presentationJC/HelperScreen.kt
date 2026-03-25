package com.example.helperjc.presentationJC

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun HelperScreen() {
    Scaffold(topBar = { AppBar() }) { innerPadding ->
        PlanItem(title = "План", innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(

) {
    TopAppBar(
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
    title: String,
    innerPadding: PaddingValues
) {
    Card (modifier = Modifier
        .padding(innerPadding)
        .border(width =  2.dp, color = Color.Red)

    ) {
        Text(text = title)
    }
}