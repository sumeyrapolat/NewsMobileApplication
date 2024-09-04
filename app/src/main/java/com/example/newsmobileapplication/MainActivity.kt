package com.example.newsmobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.newsmobileapplication.ui.navigation.Router
import com.example.newsmobileapplication.ui.theme.NewsMobileApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsMobileApplicationTheme {
                // NavController'ı hatırlıyoruz
                val navController = rememberNavController()

                // Router'ı çağırarak navigasyonu başlatıyoruz
                Router(navHostController = navController)
            }
        }
    }
}

