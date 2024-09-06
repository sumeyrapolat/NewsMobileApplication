package com.example.newsmobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsmobileapplication.ui.components.BottomBar
import com.example.newsmobileapplication.ui.components.BottomNavItem
import com.example.newsmobileapplication.ui.navigation.Router
import com.example.newsmobileapplication.ui.theme.NewsMobileApplicationTheme
import com.example.newsmobileapplication.ui.theme.Redwood
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

    // Define the two Bottom Navigation items (Home, Favorites, and Category)
    val bottomNavItems = listOf(
        BottomNavItem(
            route = "feed",
            icon = Icons.Filled.Home,
            label = "Home",
            onClick = { navController.navigate("feed") }
        ),
        BottomNavItem(
            route = "category",  // Category route corrected
            icon = Icons.Filled.Search,
            label = "Category",
            onClick = { navController.navigate("category") }  // Navigate to the correct "category" route
        ),
        BottomNavItem(
            route = "favorites",
            icon = Icons.Filled.Bookmarks,
            label = "Favorites",
            onClick = { navController.navigate("favorites") }
        )

    )

    Scaffold(
        topBar = {
            if (currentDestination == "feed" || currentDestination == "favorites" || currentDestination == "category") {
                TopAppBar(
                    title = { Text("NewsRubu+", fontWeight = FontWeight.SemiBold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Redwood, // Background color
                        titleContentColor = Color.White // Title text color
                    )
                )
            }
        },
        bottomBar = {
            if (currentDestination == "feed" || currentDestination == "favorites" || currentDestination == "category") {
                // Pass bottom navigation items to the BottomBar
                BottomBar(navController = navController, bottomNavItems = bottomNavItems, onItemClick = { navController.navigate(it) })
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
