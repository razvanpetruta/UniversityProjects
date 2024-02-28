package com.example.exam.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
fun SecondaryScreen(
    navController: NavHostController,
    viewModel: TemplateViewModel,
    modifier: Modifier = Modifier
) {
    val loading by viewModel.isLoading
    val connectivity by connectivityState()

    val types by viewModel.types.collectAsStateWithLifecycle()
    var currentType by rememberSaveable { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "REGISTER") },
            text = {
                Text(text = "Are you sure you want to register for this type?")
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    viewModel.registerForType(currentType)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        if (connectivity == ConnectionState.Available && types.isEmpty()) {
            viewModel.getTypes()
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
                        types.forEach { type ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clickable {
                                        showDialog = true
                                        currentType = type
                                    }
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = type,
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