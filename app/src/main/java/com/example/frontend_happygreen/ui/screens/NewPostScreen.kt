package com.example.frontend_happygreen.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend_happygreen.viewmodel.PostViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import java.io.File

@Composable
fun NewPostScreen(
    gruppoId: Int,
    token: String,
    navController: NavController,
    postViewModel: PostViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    var descrizione by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Nuovo Post", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = descrizione,
            onValueChange = { descrizione = it },
            label = { Text("Descrizione") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = { imagePicker.launch("image/*") }) {
            Text("Seleziona immagine (opzionale)")
        }

        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Anteprima immagine",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
        }

        Button(
            onClick = {
                if (descrizione.isBlank()) {
                    Toast.makeText(context, "Inserisci una descrizione", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val imageFile = selectedImageUri?.let { uri ->
                    try {
                        File(uri.path!!)
                    } catch (e: Exception) {
                        null
                    }
                }

                postViewModel.creaPost(
                    gruppoId = gruppoId,
                    descrizione = descrizione,
                    riconoscimento = "Oggetto finto", // da MLKit
                    latitudine = 0.0, // da GPS
                    longitudine = 0.0,
                    token = token,
                    imageFile = imageFile,
                    onSuccess = {
                        Toast.makeText(context, "Post creato!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onError = {
                        Toast.makeText(context, "Errore: $it", Toast.LENGTH_LONG).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pubblica")
        }
    }
}