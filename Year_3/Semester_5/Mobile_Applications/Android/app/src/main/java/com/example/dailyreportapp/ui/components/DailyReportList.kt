package com.example.dailyreportapp.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dailyreportapp.data.DailyReport

@Composable
fun DailyReportList(
    reports: List<DailyReport>,
    navController: NavHostController,
    onDeleteReport: (DailyReport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = reports,
            key = { report -> report.id }
        ) { report ->
            DailyReportItem(
                report = report,
                navController = navController,
                onDeleteReport = onDeleteReport
            )
        }
    }
}