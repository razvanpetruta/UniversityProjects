package com.example.androiddb.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.androiddb.ui.components.DailyReportList
import com.example.androiddb.ui.components.TopBar
import com.example.androiddb.viewmodels.DailyReportsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavHostController,
    dailyReportsViewModel: DailyReportsViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val isSaving by dailyReportsViewModel.isSaving
    var isDeleteButtonClicked by remember { mutableStateOf(false) }
    val errorState by dailyReportsViewModel.errorState

    LaunchedEffect(isSaving, isDeleteButtonClicked) {
        if (isSaving && isDeleteButtonClicked && errorState == null) {
            snackbarHostState.showSnackbar(
                "Deleting report...",
                duration = SnackbarDuration.Short,
                actionLabel = null
            )
        } else if (isDeleteButtonClicked && errorState == null) {
            snackbarHostState.showSnackbar(
                "Report deleted successfully",
                duration = SnackbarDuration.Short,
                actionLabel = "Close"
            )
        } else if (isDeleteButtonClicked) {
            snackbarHostState.showSnackbar(
                errorState!!,
                duration = SnackbarDuration.Short,
                actionLabel = "Close"
            )
            dailyReportsViewModel.clearErrorState()
        }
        isDeleteButtonClicked = false
    }

    Scaffold(
        topBar = { TopBar() },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DailyReportList(
                reports = dailyReportsViewModel.reportsList.collectAsStateWithLifecycle(
                    initialValue = emptyList()
                ),
                navController = navController,
                onDeleteReport = { report ->
                    isDeleteButtonClicked = true
                    scope.launch {
                        dailyReportsViewModel.removeReport(report)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .align(Alignment.TopStart)
            )

            FloatingActionButton(
                onClick = {
                    navController.navigate("manageReportPage")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Report"
                )
            }
        }
    }
}