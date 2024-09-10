package com.example.newsmobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsmobileapplication.model.repository.AuthRepository
import com.example.newsmobileapplication.ui.components.BottomBar
import com.example.newsmobileapplication.ui.components.BottomNavItem
import com.example.newsmobileapplication.ui.navigation.Router
import com.example.newsmobileapplication.ui.theme.NavyBlue
import com.example.newsmobileapplication.ui.theme.NewsMobileApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsMobileApplicationTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController, authRepository = authRepository)
            }
        }
    }
}


@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavHostController, authRepository: AuthRepository) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val showMenu = remember { mutableStateOf(false) } // Menü için durum

    // Define the Bottom Navigation items (Home, Favorites, and Category)
    val bottomNavItems = listOf(
        BottomNavItem(
            route = "feed",
            icon = Icons.Filled.Home,
            label = "Home",
            onClick = { navController.navigate("feed") }
        ),
        BottomNavItem(
            route = "category",
            icon = Icons.Filled.TravelExplore,
            label = "Category",
            onClick = { navController.navigate("category") }
        ),
        BottomNavItem(
            route = "favorites",
            icon = Icons.Filled.Bookmarks,
            label = "Saved",
            onClick = { navController.navigate("favorites") }
        )
    )

    Scaffold(
        topBar = {
            if (currentDestination == "feed" || currentDestination == "favorites" || currentDestination == "category") {
                TopAppBar(
                    title = { Text("\uD835\uDC11\uD835\uDC14\uD835\uDC01\uD835\uDC14+ \uD835\uDC0D\uD835\uDC04\uD835\uDC16\uD835\uDC12") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White, // Background color
                        titleContentColor = Color.Black // Title text color
                    ),
                    actions = {
                        IconButton(onClick = { showMenu.value = true }) {
                            Icon(Icons.Default.Menu,
                                contentDescription = "Menu",
                                modifier = Modifier.size(26.dp))
                        }

                        DropdownMenu(
                            expanded = showMenu.value,
                            onDismissRequest = { showMenu.value = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    ) {
                                        Text("Sign Out", color = NavyBlue, fontWeight = FontWeight.SemiBold, fontSize = 20.sp) // Text rengi ayarlanıyor
                                    }
                                       },
                                onClick = {
                                    authRepository.signOut()
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                        }
                                    }
                                    showMenu.value = false
                                }
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentDestination == "feed" || currentDestination == "favorites" || currentDestination == "category") {
                BottomBar(navController = navController, bottomNavItems = bottomNavItems, onItemClick = { navController.navigate(it) })
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Router(navController = navController, authRepository = authRepository)
            }
        }
    )
}
