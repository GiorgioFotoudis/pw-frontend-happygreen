package com.example.frontend_happygreen.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend_happygreen.viewmodel.PostViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import java.io.File
import android.util.Log
import com.example.frontend_happygreen.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    gruppoId: Int,
    token: String,
    navController: NavController,
    postViewModel: PostViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var descrizione by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedImageUri = uri
    }

    // Debug: Log del token e gruppoId
    LaunchedEffect(token, gruppoId) {
        Log.d("NewPostScreen", "Token: $token")
        Log.d("NewPostScreen", "GruppoId: $gruppoId")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EcoGreen50,
                        EcoGreen100,
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top App Bar personalizzata
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = EcoGreen600
                        )
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Torna indietro",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nuovo Post",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EcoGreen800
                            )
                        )
                        Text(
                            text = "Condividi la tua azione sostenibile",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = EcoGreen600
                            )
                        )
                    }

                    // Icona decorativa
                    Card(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = EcoGreen500
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🌱",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Contenuto principale
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it })
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    // Card per la descrizione
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Description,
                                    contentDescription = null,
                                    tint = EcoGreen500,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Descrizione",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = EcoGreen800
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = descrizione,
                                onValueChange = { descrizione = it },
                                placeholder = {
                                    Text(
                                        "Racconta la tua azione per l'ambiente...",
                                        color = EcoGreen400
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EcoGreen500,
                                    focusedLabelColor = EcoGreen500,
                                    cursorColor = EcoGreen500,
                                    unfocusedBorderColor = EcoGreen200
                                ),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                ),
                                enabled = !isLoading,
                                maxLines = 4
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Card per l'immagine
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = EcoGreen500,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Immagine",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = EcoGreen800
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "(Opzionale)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EcoGreen500
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            if (selectedImageUri != null) {
                                // Immagine selezionata
                                Box {
                                    Image(
                                        painter = rememberAsyncImagePainter(selectedImageUri),
                                        contentDescription = "Anteprima immagine",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Pulsante per rimuovere l'immagine
                                    FloatingActionButton(
                                        onClick = { selectedImageUri = null },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .size(40.dp),
                                        containerColor = ErrorRed,
                                        contentColor = Color.White
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Rimuovi immagine",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            } else {
                                // Pulsante per selezionare immagine
                                OutlinedButton(
                                    onClick = { imagePicker.launch("image/*") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = EcoGreen600
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(EcoGreen400, EcoGreen600)
                                        )
                                    ),
                                    enabled = !isLoading
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Aggiungi foto",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Pulsante di pubblicazione
                    Button(
                        onClick = {
                            if (descrizione.isBlank()) {
                                Toast.makeText(context, "Inserisci una descrizione", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            isLoading = true
                            Log.d("NewPostScreen", "Iniziando creazione post...")

                            val imageFile = selectedImageUri?.let { uri ->
                                try {
                                    File(uri.path!!)
                                } catch (e: Exception) {
                                    Log.e("NewPostScreen", "Errore nel processare l'immagine: ${e.message}")
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
                                    Log.d("NewPostScreen", "Post creato con successo!")
                                    isLoading = false
                                    Toast.makeText(context, "Post creato con successo!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                },
                                onError = { errorMessage ->
                                    Log.e("NewPostScreen", "Errore nella creazione del post: $errorMessage")
                                    isLoading = false
                                    Toast.makeText(context, "Errore: $errorMessage", Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen500,
                            disabledContainerColor = EcoGreen300
                        ),
                        enabled = !isLoading && descrizione.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Pubblicando...",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        } else {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Pubblica Post",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Footer motivazionale
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = EcoGreen50
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "🌍 Ogni piccola azione conta per il nostro pianeta!",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = EcoGreen600,
                                fontWeight = FontWeight.Medium
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}