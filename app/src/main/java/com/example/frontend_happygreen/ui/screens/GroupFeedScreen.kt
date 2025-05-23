package com.example.frontend_happygreen.ui.screens

import CommentoRichiesta
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend_happygreen.viewmodel.AuthViewModel
import com.example.frontend_happygreen.viewmodel.CommentViewModel
import com.example.frontend_happygreen.viewmodel.PostViewModel
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupFeedScreen(
    gruppoId: Int,
    token: String,
    navController: NavController,
    postViewModel: PostViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val posts by postViewModel.posts.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    // Debug logs
    LaunchedEffect(gruppoId, token) {
        Log.d("GroupFeedScreen", "GruppoId: $gruppoId, Token: ${token.take(20)}...")
        isRefreshing = true
        postViewModel.loadPostsByGroup(gruppoId, token)
        isRefreshing = false
    }

    // Log quando cambiano i posts
    LaunchedEffect(posts) {
        Log.d("GroupFeedScreen", "Posts caricati: ${posts.size}")
        posts.forEach { post ->
            Log.d("GroupFeedScreen", "Post ID: ${post.id}, Descrizione: ${post.descrizione}")
        }
    }

    val commentViewModel: CommentViewModel = viewModel()
    val commenti by commentViewModel.commenti.collectAsState()

    LaunchedEffect(token) {
        commentViewModel.caricaCommenti(token)
        // Carica anche il profilo utente se non è già caricato
        if (authViewModel.userProfile.value == null) {
            authViewModel.loadUserProfile(token)
        }
    }

    // Funzione per ricaricare i dati
    fun refreshData() {
        isRefreshing = true
        postViewModel.loadPostsByGroup(gruppoId, token)
        commentViewModel.caricaCommenti(token)
        isRefreshing = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bacheca del gruppo") },
                actions = {
                    IconButton(onClick = { refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Aggiorna")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("group_create_post/$gruppoId")
            },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crea nuovo post")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isRefreshing) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (posts.isEmpty() && !isRefreshing) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Nessun post in questo gruppo.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { refreshData() }) {
                            Text("Riprova")
                        }
                    }
                }
            } else {
                posts.forEach { post ->
                    var nuovoCommento by remember { mutableStateOf("") }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            // Gestione sicura dell'immagine
                            post.immagine?.let { imageUrl ->
                                if (imageUrl.isNotBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(imageUrl),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(180.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }

                            Text(
                                "Autore: ${post.autore_username ?: "Sconosciuto"}",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(post.descrizione ?: "Nessuna descrizione")
                            Text(
                                "Posizione: ${post.latitudine ?: 0.0}, ${post.longitudine ?: 0.0}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "Data: ${post.data_pubblicazione?.take(10) ?: "Data non disponibile"}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            Text("Commenti:", style = MaterialTheme.typography.titleSmall)

                            val commentiDelPost = commenti.filter { it.post == post.id }

                            if (commentiDelPost.isEmpty()) {
                                Text("Nessun commento ancora.")
                            } else {
                                commentiDelPost.forEach { commento ->
                                    Text(
                                        "- ${commento.autore_username}: ${commento.testo}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = nuovoCommento,
                                onValueChange = { nuovoCommento = it },
                                label = { Text("Scrivi un commento...") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    val userId = authViewModel.userProfile.value?.id
                                    Log.d("GroupFeedScreen", "Button clicked - Token: ${token.isNotBlank()}, UserId: $userId, Commento: '$nuovoCommento'")
                                    if (token.isNotBlank() && userId != null && nuovoCommento.isNotBlank()) {
                                        Log.d("GroupFeedScreen", "Chiamando aggiungiCommento...")
                                        commentViewModel.aggiungiCommento(
                                            CommentoRichiesta(
                                                post = post.id,
                                                testo = nuovoCommento,
                                                autore = userId
                                            ),
                                            token
                                        ) {
                                            Log.d("GroupFeedScreen", "Callback eseguito, ricaricando commenti...")
                                            nuovoCommento = ""
                                            commentViewModel.caricaCommenti(token)
                                        }
                                    }else{
                                        Log.d("GroupFeedScreen", "Condizioni non soddisfatte per inviare commento")
                                    }
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Invia")
                            }
                        }
                    }
                }
            }
        }
    }
}