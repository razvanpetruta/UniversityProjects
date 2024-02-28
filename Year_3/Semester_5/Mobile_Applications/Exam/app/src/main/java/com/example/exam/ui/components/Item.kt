package com.example.exam.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exam.data.Listing
import com.example.exam.viewmodels.TemplateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun Item(
    item: Listing,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: TemplateViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val currentListing by viewModel.currentListing

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "INFO") },
            text = {
                Text(text = "Id: ${currentListing.id}\nName: ${currentListing.name}\nDate: ${currentListing.date}\nDetails: ${currentListing.details}\nStatus: ${currentListing.status}\nViewers: ${currentListing.viewers}\nType: ${currentListing.type}")
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Close")
                }
            },
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.getListingById(item.id)
                    showDialog = true
                }
                .padding(16.dp)
        ) {
            Text(
                text = item.id.toString(),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Name: ${item.name}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Date: ${item.date}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Type: ${item.type}")
        }
    }
}