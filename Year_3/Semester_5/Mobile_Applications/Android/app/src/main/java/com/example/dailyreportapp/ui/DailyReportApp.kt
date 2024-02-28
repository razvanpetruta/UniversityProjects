package com.example.dailyreportapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailyreportapp.ui.screens.AddReportPage
import com.example.dailyreportapp.ui.screens.MainPage
import com.example.dailyreportapp.ui.screens.UpdateReportPage
import com.example.dailyreportapp.viewmodels.DailyReportsViewModel
import java.util.UUID

@Composable
fun DailyReportApp() {
    val navController = rememberNavController()
    val dailyReportsViewModel: DailyReportsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "mainPage"
    ) {
        composable(route = "mainPage") {
            MainPage(navController, dailyReportsViewModel)
        }

        composable(route = "addPage") {
            AddReportPage(navController, dailyReportsViewModel)
        }

        composable(
            route = "updateReport/{reportId}",
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId")

            if (reportId != null) {
                val uuid = UUID.fromString(reportId)
                val dailyReport = dailyReportsViewModel.getReportById(uuid)

                if (dailyReport != null) {
                    UpdateReportPage(navController, dailyReportsViewModel, dailyReport)
                }
            }
        }
    }
}