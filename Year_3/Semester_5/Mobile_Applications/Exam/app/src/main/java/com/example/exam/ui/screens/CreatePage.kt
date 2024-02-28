package com.example.exam.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.exam.data.Listing
import com.example.exam.viewmodels.TemplateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePage(
    navController: NavController,
    viewModel: TemplateViewModel
) {
    var name by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var details by rememberSaveable { mutableStateOf("") }
    var status by rememberSaveable { mutableStateOf("") }
    var viewers by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }

    val loading by viewModel.isLoading

    if (!loading) {
        Spacer(modifier = Modifier.padding(16.dp))

        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Details") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Status") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                value = viewers,
                onValueChange = { viewers = it },
                label = { Text("Viewers") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            TextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = {
                    if (name.isNotEmpty() && date.isNotEmpty() && details.isNotEmpty() && status.isNotEmpty() && viewers.isNotEmpty() && type.isNotEmpty()) {
                        val id = (1000000..9999999).random()
                        val viewersAsInt = viewers.toIntOrNull() ?: 0

                        viewModel.addItem(
                            Listing(
                                id,
                                name,
                                date,
                                details,
                                status,
                                viewersAsInt,
                                type,
                                needsSync = false
                            )
                        )
                    } else {
                        viewModel.setMessage("Some fields are invalid")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Entity")
            }
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