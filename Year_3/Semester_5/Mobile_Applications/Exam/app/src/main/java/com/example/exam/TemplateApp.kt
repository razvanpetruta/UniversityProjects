package com.example.exam

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exam.ui.components.BottomNavBar
import com.example.exam.ui.screens.AdditionalScreen
import com.example.exam.ui.screens.CreatePage
import com.example.exam.ui.screens.MainScreen
import com.example.exam.ui.screens.SecondaryScreen
import com.example.exam.viewmodels.TemplateViewModel

enum class AppRoutes(val route: String) {
    MainScreen("main"),
    SecondaryScreen("secondary"),
    AdditionalScreen("additional"),
    CreatePage("createPage")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateApp() {
    val navController = rememberNavController()
    val templateViewModel: TemplateViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController, viewModel = templateViewModel) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.MainScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppRoutes.MainScreen.route) {
                MainScreen(navController = navController, viewModel = templateViewModel)
            }

            composable(route = AppRoutes.SecondaryScreen.route) {
                SecondaryScreen(navController = navController, viewModel = templateViewModel)
            }

            composable(route = AppRoutes.AdditionalScreen.route) {
                AdditionalScreen(navController = navController, viewModel = templateViewModel)
            }

            composable(route = AppRoutes.CreatePage.route) {
                CreatePage(navController = navController, viewModel = templateViewModel)
            }
        }
    }
}