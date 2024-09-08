package com.example.newsmobileapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.newsmobileapplication.ui.theme.KhasmirBlue
import com.example.newsmobileapplication.ui.theme.Martinique
import com.example.newsmobileapplication.ui.theme.Redwood

@Composable
fun BottomBar(navController: NavController, bottomNavItems: List<BottomNavItem>, onItemClick: (String) -> Unit) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20)
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            tonalElevation = 0.dp, // Flatten the elevation if needed
            modifier = Modifier.height(88.dp) // Standard height for a compact bottom bar
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.route,
                            tint = if (selected) KhasmirBlue else Color.Gray
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (currentRoute != item.route) {
                            onItemClick(item.route)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KhasmirBlue,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    }
}


data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit
)
