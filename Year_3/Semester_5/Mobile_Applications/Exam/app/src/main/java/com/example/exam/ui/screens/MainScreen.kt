package com.example.exam.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.exam.AppRoutes
import com.example.exam.connectivityState
import com.example.exam.ui.components.ItemsList
import com.example.exam.viewmodels.TemplateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: TemplateViewModel
) {
    val loading by viewModel.isLoading
    val connectivity by connectivityState()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                if (!loading) {
                    Button(
                        onClick = {
                            navController.navigate(AppRoutes.CreatePage.route)
                        }) {
                        Text(text = "Create Entity")
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    ItemsList(
                        items = viewModel.listings.collectAsStateWithLifecycle(),
                        navController = navController,
                        viewModel = viewModel
                    )
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