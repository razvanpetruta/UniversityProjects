package com.example.exam.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.exam.ConnectionState
import com.example.exam.connectivityState
import com.example.exam.viewmodels.TemplateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun AdditionalScreen(
    navController: NavHostController,
    viewModel: TemplateViewModel
) {
    val loading by viewModel.isLoading
    val connectivity by connectivityState()

    val groupedByMonth by viewModel.monthlyViewersSum.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (connectivity == ConnectionState.Available && groupedByMonth.isEmpty()) {
            viewModel.getInterest()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                if (!loading) {
                    if (connectivity == ConnectionState.Available) {
                        groupedByMonth.forEach { el ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Month: ${el.key}, Viewers: ${el.value}",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "No internet connection, please check your network settings",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
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
}