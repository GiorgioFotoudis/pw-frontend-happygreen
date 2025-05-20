package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.viewmodel.GroupViewModel

@Composable
fun GroupCreateScreen(
    groupViewModel: GroupViewModel = viewModel(),
    token: String
) {
    val gruppi by groupViewModel.gruppi.collectAsState()

    LaunchedEffect(Unit) {
        groupViewModel.loadAllGroups(token)
    }

    LazyColumn {
        items(gruppi) { gruppo ->
            Text(text = gruppo.nome)
            Button(onClick = {
                groupViewModel.iscrivitiAGruppo(gruppo.id, token)
            }) {
                Text("Iscriviti")
            }
        }
    }
}
