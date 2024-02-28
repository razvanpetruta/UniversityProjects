package com.example.exam.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.exam.data.Listing
import com.example.exam.viewmodels.TemplateViewModel

@Composable
fun ItemsList(
    items: State<List<Listing>>,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: TemplateViewModel
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = items.value,
            key = { item: Listing -> item.id }
        ) { item: Listing ->
            Item(item = item, navController = navController, viewModel = viewModel)
        }
    }
}