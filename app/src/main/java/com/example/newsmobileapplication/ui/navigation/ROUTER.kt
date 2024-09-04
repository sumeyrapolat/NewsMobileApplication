package com.example.newsmobileapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsmobileapplication.ui.screens.FeedScreen
import com.example.newsmobileapplication.ui.screens.LoginScreen
import com.example.newsmobileapplication.ui.screens.SignUpScreen
import com.example.newsmobileapplication.viewmodel.LoginViewModel
import com.example.newsmobileapplication.viewmodel.SignUpViewModel

@Composable
fun Router(navController: NavHostController){

    NavHost(navController = navController, startDestination = "signup"  ) {

        composable("signup") {
            val signUpViewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(navController, signUpViewModel)
        }
        composable("login") {
            val signInViewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController, signInViewModel)
        }

        composable("feed") { FeedScreen(navController) }

    }

}