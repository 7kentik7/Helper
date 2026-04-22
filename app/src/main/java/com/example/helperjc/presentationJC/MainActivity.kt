package com.example.helperjc.presentationJC

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.helperjc.presentationJC.navigation.HelperNavGraph
import com.example.helperjc.presentationJC.theme.HelperJCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelperJCTheme {
                HelperNavGraph()
            }
        }
    }
}


