package com.example.yumidang

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.yumidang.ui.YumidangMainScreen
import com.example.yumidang.ui.theme.YumidangTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YumidangTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    YumidangMainScreen()
                }
            }
        }
    }
}
