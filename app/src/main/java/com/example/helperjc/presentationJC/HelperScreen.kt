package com.example.helperjc.presentationJC

import android.widget.ProgressBar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.nio.file.WatchEvent

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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .border(width = 1.dp, color = Color.Blue),
        shape = RoundedCornerShape(8.dp),
        onClick = {}
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = "20.02.2027")
            Text(text = title)
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Red,
                progress = { 1f })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Text(text = "Всего")
                    Text(text = "Выполнено")
                }

                IconButton(onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = null)
                }
            }
        }
    }
}