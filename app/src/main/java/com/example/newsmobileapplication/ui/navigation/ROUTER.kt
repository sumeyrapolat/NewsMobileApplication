package com.example.newsmobileapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsmobileapplication.ui.screens.FavoriteScreen
import com.example.newsmobileapplication.ui.screens.FeedScreen
import com.example.newsmobileapplication.ui.screens.LoginScreen
import com.example.newsmobileapplication.ui.screens.NewsDetailScreen
import com.example.newsmobileapplication.ui.screens.SignUpScreen
import com.example.newsmobileapplication.viewmodel.LoginViewModel
import com.example.newsmobileapplication.viewmodel.SignUpViewModel


@Composable
fun Router(navController: NavHostController){

    NavHost(navController = navController, startDestination = "feed") {

        composable("signup") {
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(navController, signUpViewModel)
        }
        composable("login") {
            val signInViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, signInViewModel)
        }

        composable("feed") {
            FeedScreen(navController)
        }

        composable("favorites") {
            FavoriteScreen(navController)
        }

        composable(
            "newsDetail/{newsItemId}",
            arguments = listOf(
                navArgument("newsItemId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val newsItemId = backStackEntry.arguments?.getString("newsItemId")
            NewsDetailScreen(navController = navController, newsItemId = newsItemId ?: "")
        }
    }
}