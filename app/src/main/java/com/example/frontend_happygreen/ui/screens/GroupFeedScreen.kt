package com.example.frontend_happygreen.ui.screens

import com.example.frontend_happygreen.viewmodel.CommentViewModel
import CommentoRichiesta
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.frontend_happygreen.viewmodel.PostViewModel

@Composable
fun GroupFeedScreen(
    gruppoId: Int,
    token: String,
    navController: NavController,
    postViewModel: PostViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val posts by postViewModel.posts.collectAsState()

    LaunchedEffect(gruppoId, token) {
        token.let{
            postViewModel.loadPostsByGroup(gruppoId, it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Bacheca del gruppo", style = MaterialTheme.typography.headlineSmall)

        if (posts.isEmpty()) {
            Text("Nessun post in questo gruppo.")
        } else {
            /*posts.forEach { post ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Immagine del post
                        Image(
                            painter = rememberAsyncImagePainter(post.immagine),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Autore: ${post.autore_username}", style = MaterialTheme.typography.labelMedium)
                        Text(post.descrizione)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Posizione: ${post.latitudine}, ${post.longitudine}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Data: ${post.data_pubblicazione.take(10)}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }*/
            val commentViewModel: CommentViewModel = viewModel()
            val commenti by commentViewModel.commenti.collectAsState()

            LaunchedEffect(token) {
                token?.let { commentViewModel.caricaCommenti(it) }
            }

            posts.forEach { post ->
                var nuovoCommento by remember { mutableStateOf("") }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Image(
                            painter = rememberAsyncImagePainter(post.immagine),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Autore: ${post.autore_username}", style = MaterialTheme.typography.labelMedium)
                        Text(post.descrizione)
                        Text("Posizione: ${post.latitudine}, ${post.longitudine}", style = MaterialTheme.typography.bodySmall)
                        Text("Data: ${post.data_pubblicazione.take(10)}", style = MaterialTheme.typography.bodySmall)

                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Commenti:", style = MaterialTheme.typography.titleSmall)

                        val commentiDelPost = commenti.filter { it.post == post.id }

                        if (commentiDelPost.isEmpty()) {
                            Text("Nessun commento ancora.")
                        } else {
                            commentiDelPost.forEach { commento ->
                                Text("- ${commento.autore_username}: ${commento.testo}", style = MaterialTheme.typography.bodySmall)
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
                                if (token != null && userId != null && nuovoCommento.isNotBlank()) {
                                    commentViewModel.aggiungiCommento(
                                        CommentoRichiesta(
                                            post = post.id,
                                            testo = nuovoCommento,
                                            autore = userId
                                        ),
                                        token
                                    ) {
                                        nuovoCommento = ""
                                        commentViewModel.caricaCommenti(token) // aggiorna
                                    }
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