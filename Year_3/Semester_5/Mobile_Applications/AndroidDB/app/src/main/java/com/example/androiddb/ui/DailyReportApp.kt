package com.example.androiddb.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.androiddb.ui.screens.MainPage
import com.example.androiddb.ui.screens.ManageReportPage
import com.example.androiddb.viewmodels.DailyReportsViewModel
import java.util.UUID

@Composable
fun DailyReportApp() {
    val navController = rememberNavController()
    val dailyReportsViewModel: DailyReportsViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "mainPage"
    ) {
        composable(route = "mainPage") {
            MainPage(navController, dailyReportsViewModel)
        }

        composable(route = "manageReportPage") {
            ManageReportPage(navController, dailyReportsViewModel)
        }

        composable(
            route = "manageReportPage/{reportId}",
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId")

            if (reportId != null) {
                val uuid = UUID.fromString(reportId)
                ManageReportPage(navController, dailyReportsViewModel, uuid)
            }
        }
    }
}