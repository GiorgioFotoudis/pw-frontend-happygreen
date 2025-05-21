package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.viewmodel.GroupViewModel

@Composable
fun GroupCreateFormScreen(
    token: String,
    groupViewModel: GroupViewModel = viewModel(),
    onGroupCreated: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var descrizione by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        Text(text = "Crea un nuovo gruppo", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome del gruppo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descrizione,
            onValueChange = { descrizione = it },
            label = { Text("Descrizione") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                isLoading = true
                groupViewModel.creaGruppo(nome, descrizione, token) { success ->
                    isLoading = false
                    if (success) {
                        message = "Gruppo creato con successo!"
                        onGroupCreated()
                    } else {
                        message = "Errore durante la creazione del gruppo"
                    }
                }
            },
            enabled = nome.isNotBlank() && descrizione.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crea gruppo")
        }

        message?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(it)
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}