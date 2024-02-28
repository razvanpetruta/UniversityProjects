package com.example.exam.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SettingsAccessibility
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.exam.AppRoutes
import com.example.exam.connectivityState
import com.example.exam.viewmodels.TemplateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun BottomNavBar(navController: NavController, viewModel: TemplateViewModel) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        val activeTab by viewModel.activeTab
        val context = LocalContext.current
        val message by viewModel.message
        val connection by connectivityState()

        LaunchedEffect(message) {
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.setMessage("")
            }
        }

        LaunchedEffect(connection) {
            viewModel.updateConnectionState(connection)
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    navController.navigate(AppRoutes.MainScreen.route)
                    viewModel.setActiveTab(AppRoutes.MainScreen)
                }) {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Main",
                        tint = if (activeTab == AppRoutes.MainScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    "Main",
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    navController.navigate(AppRoutes.SecondaryScreen.route)
                    viewModel.setActiveTab(AppRoutes.SecondaryScreen)
                }) {
                    Icon(
                        Icons.Filled.Today,
                        contentDescription = "Secondary",
                        tint = if (activeTab == AppRoutes.SecondaryScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text("Secondary", style = MaterialTheme.typography.labelSmall)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    navController.navigate(AppRoutes.AdditionalScreen.route)
                    viewModel.setActiveTab(AppRoutes.AdditionalScreen)
                }) {
                    Icon(
                        Icons.Filled.SettingsAccessibility,
                        contentDescription = "Additional",
                        tint = if (activeTab == AppRoutes.AdditionalScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text("Additional", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}