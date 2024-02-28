package com.example.androiddb.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.androiddb.domain.model.DailyReport

@Composable
fun DailyReportList(
    reports: State<List<DailyReport>>,
    navController: NavHostController,
    onDeleteReport: (DailyReport) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = reports.value,
            key = { report: DailyReport -> report.id }
        ) { report: DailyReport ->
            DailyReportItem(
                report = report,
                navController = navController,
                onDeleteReport = onDeleteReport
            )
        }
    }
}