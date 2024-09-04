package com.example.newsmobileapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newsmobileapplication.ui.screens.SignUpScreen

@Composable
fun Router(navHostController: NavHostController){

    NavHost(navController = navHostController, startDestination = "signup"  ) {

        composable("signup") {
            SignUpScreen(navHostController)
        }
    }

}