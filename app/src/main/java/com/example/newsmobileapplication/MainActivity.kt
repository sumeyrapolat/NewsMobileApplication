package com.example.newsmobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsmobileapplication.ui.navigation.Router
import com.example.newsmobileapplication.ui.theme.NewsMobileApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsMobileApplicationTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentDestination == "feed" || currentDestination == "favorites") {
                TopAppBar(
                    title = { Text("NewsRubu+") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White, // Beyaz arka plan
                        titleContentColor = Color.Black // Siyah başlık rengi
                    )
                )
            }
        },
        bottomBar = {
            if (currentDestination == "feed" || currentDestination == "favorites") {
                BottomBar(navController = navController)
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Router(navController = navController)
            }
        }
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val currentDestination by navController.currentBackStackEntryAsState()

    BottomNavigation(
        backgroundColor = Color.White, // Beyaz arka plan
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            selected = currentDestination?.destination?.route == "feed",
            selectedContentColor = Color.Black,  // Seçili ikon siyah
            unselectedContentColor = Color.Gray, // Seçili olmayan ikon gri
            onClick = {
                if (currentDestination?.destination?.route != "feed") {
                    navController.navigate("feed") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            selected = currentDestination?.destination?.route == "favorites",
            selectedContentColor = Color.Black,  // Seçili ikon siyah
            unselectedContentColor = Color.Gray, // Seçili olmayan ikon gri
            onClick = {
                if (currentDestination?.destination?.route != "favorites") {
                    navController.navigate("favorites") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
    }
}
