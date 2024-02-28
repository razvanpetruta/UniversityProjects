package com.example.dailyreportapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailyreportapp.data.DailyReport
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DailyReportItem(
    report: DailyReport,
    navController: NavHostController,
    onDeleteReport: (DailyReport) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat =
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Customize the date format
    val formattedDate = dateFormat.format(report.date)

    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("updateReport/${report.id}")
                }
                .padding(16.dp)
        ) {
            Text(text = report.title, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Rating: ${report.rating}")
            Text(text = "Updated on $formattedDate")
            IconButton(
                onClick = {
                    isDeleteDialogOpen = true
                },
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (isDeleteDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                isDeleteDialogOpen = false
            },
            title = { Text("Delete Report") },
            text = {
                Column {
                    Text("Are you sure you want to delete this report?")
                    Text("Title: ${report.title}")
                    Text("Date: $formattedDate")
                    Text("Rating: ${report.rating}")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteReport(report)
                        isDeleteDialogOpen = false
                    }
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        isDeleteDialogOpen = false
                    }
                ) {
                    Text("No")
                }
            }
        )
    }
}