package com.example.androiddb.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.androiddb.domain.model.DailyReport
import com.example.androiddb.viewmodels.DailyReportsViewModel
import java.util.Calendar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageReportPage(
    navController: NavHostController,
    dailyReportsViewModel: DailyReportsViewModel,
    reportId: UUID? = null
) {
    var title by rememberSaveable { mutableStateOf("") }
    var gratitude by rememberSaveable { mutableStateOf("") }
    var accomplishments by rememberSaveable { mutableStateOf("") }
    var shortcomings by rememberSaveable { mutableStateOf("") }
    var improvementAreas by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf(3) }

    var titleError by rememberSaveable { mutableStateOf<String?>(null) }
    var gratitudeError by rememberSaveable { mutableStateOf<String?>(null) }
    var accomplishmentsError by rememberSaveable { mutableStateOf<String?>(null) }
    var shortcomingsError by rememberSaveable { mutableStateOf<String?>(null) }
    var improvementAreasError by rememberSaveable { mutableStateOf<String?>(null) }

    val isUpdateReport = reportId != null

    val isSaving by dailyReportsViewModel.isSaving
    var isSaveButtonClicked by remember { mutableStateOf(false) }
    val errorState by dailyReportsViewModel.errorState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (reportId != null) {
            dailyReportsViewModel.getReportById(reportId)
                .collect { report ->
                    title = report.title
                    gratitude = report.gratitude
                    accomplishments = report.accomplishments
                    shortcomings = report.shortcomings
                    improvementAreas = report.improvementAreas
                    rating = report.rating
                }
        }
    }

    LaunchedEffect(isSaving, isSaveButtonClicked) {
        if (!isSaving && isSaveButtonClicked && errorState == null) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(errorState) {
        errorState?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
            dailyReportsViewModel.clearErrorState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isUpdateReport) "Update Daily Report" else "Add Daily Report") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        },
        modifier = Modifier
            .padding(16.dp)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (!isSaving) {
                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = if (it.isEmpty()) "Title is required" else null
                    },
                    isError = titleError != null,
                    singleLine = true,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (titleError != null) {
                    Text(text = titleError!!, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = gratitude,
                    onValueChange = {
                        gratitude = it
                        gratitudeError = if (it.isEmpty()) "Gratitude is required" else null
                    },
                    isError = gratitudeError != null,
                    singleLine = false,
                    label = { Text("Gratitude") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (gratitudeError != null) {
                    Text(text = gratitudeError!!, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = accomplishments,
                    onValueChange = {
                        accomplishments = it
                        accomplishmentsError =
                            if (it.isEmpty()) "Accomplishments are required" else null
                    },
                    isError = accomplishmentsError != null,
                    singleLine = false,
                    label = { Text("Accomplishments") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (accomplishmentsError != null) {
                    Text(text = accomplishmentsError!!, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = shortcomings,
                    onValueChange = {
                        shortcomings = it
                        shortcomingsError = if (it.isEmpty()) "Shortcomings are required" else null
                    },
                    isError = shortcomingsError != null,
                    singleLine = false,
                    label = { Text("Shortcomings") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (shortcomingsError != null) {
                    Text(text = shortcomingsError!!, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = improvementAreas,
                    onValueChange = {
                        improvementAreas = it
                        improvementAreasError =
                            if (it.isEmpty()) "Improvement Areas are required" else null
                    },
                    isError = improvementAreasError != null,
                    singleLine = false,
                    label = { Text("Improvement Areas") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (improvementAreasError != null) {
                    Text(text = improvementAreasError!!, color = Color.Red)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Rating: $rating", fontSize = 20.sp)
                Slider(
                    value = rating.toFloat(),
                    onValueChange = { rating = it.toInt() },
                    valueRange = 1f..5f,
                    steps = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        titleError = if (title.isEmpty()) "Title is required" else null
                        gratitudeError = if (gratitude.isEmpty()) "Gratitude is required" else null
                        accomplishmentsError =
                            if (accomplishments.isEmpty()) "Accomplishments are required" else null
                        shortcomingsError =
                            if (shortcomings.isEmpty()) "Shortcomings are required" else null
                        improvementAreasError =
                            if (improvementAreas.isEmpty()) "Improvement Areas are required" else null

                        if (titleError != null ||
                            gratitudeError != null ||
                            accomplishmentsError != null ||
                            shortcomingsError != null ||
                            improvementAreasError != null
                        ) {
                            return@Button
                        }

                        isSaveButtonClicked = true

                        val newReport = DailyReport(
                            id = reportId ?: UUID.randomUUID(),
                            title = title,
                            gratitude = gratitude,
                            accomplishments = accomplishments,
                            shortcomings = shortcomings,
                            improvementAreas = improvementAreas,
                            rating = rating,
                            date = Calendar.getInstance().time
                        )

                        if (isUpdateReport) {
                            dailyReportsViewModel.updateReport(newReport)
                        } else {
                            dailyReportsViewModel.addReport(newReport)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isUpdateReport) "Update Report" else "Save Report")
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}