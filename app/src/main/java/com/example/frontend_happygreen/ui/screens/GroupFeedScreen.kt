package com.example.frontend_happygreen.ui.screens

import CommentoRichiesta
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontend_happygreen.viewmodel.AuthViewModel
import com.example.frontend_happygreen.viewmodel.CommentViewModel
import com.example.frontend_happygreen.viewmodel.PostViewModel
import com.example.frontend_happygreen.ui.theme.*
import android.util.Log
import androidx.compose.ui.unit.sp
import com.example.frontend_happygreen.data.model.CommentoDto
import com.example.frontend_happygreen.data.model.PostDto

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

    val commentViewModel: CommentViewModel = viewModel()
    val commenti by commentViewModel.commenti.collectAsState()

    // Debug logs e caricamento iniziale
    LaunchedEffect(gruppoId, token) {
        Log.d("GroupFeedScreen", "GruppoId: $gruppoId, Token: ${token.take(20)}...")
        isRefreshing = true
        postViewModel.loadPostsByGroup(gruppoId, token)
        commentViewModel.caricaCommenti(token) {
            isRefreshing = false
        }

        // Carica anche il profilo utente se non è già caricato
        if (authViewModel.userProfile.value == null) {
            authViewModel.loadUserProfile(token)
        }
        isRefreshing = false
    }

    // Log quando cambiano i posts
    LaunchedEffect(posts) {
        Log.d("GroupFeedScreen", "Posts caricati: ${posts.size}")
        posts.forEach { post ->
            Log.d("GroupFeedScreen", "Post ID: ${post.id}, Descrizione: ${post.descrizione}")
        }
    }

    // Funzione per ricaricare i dati
    fun refreshData() {
        isRefreshing = true
        postViewModel.loadPostsByGroup(gruppoId, token)
        commentViewModel.caricaCommenti(token) {
            isRefreshing = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        EcoGreen50,
                        Color.White
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Groups,
                                contentDescription = null,
                                tint = EcoGreen600,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "Bacheca del gruppo",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = EcoGreen800
                                )
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { refreshData() },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(EcoGreen100)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Aggiorna",
                                tint = EcoGreen600
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        navController.navigate("group_create_post/$gruppoId")
                    },
                    containerColor = EcoGreen500,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Crea nuovo post",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Nuovo Post",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Indicatore di caricamento
                if (isRefreshing) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            ),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        color = EcoGreen500,
                                        strokeWidth = 3.dp
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        "Caricamento post...",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = EcoGreen600
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // Stato vuoto
                if (posts.isEmpty() && !isRefreshing) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Icona grande
                                Card(
                                    modifier = Modifier.size(80.dp),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = EcoGreen100
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "🌱",
                                            style = MaterialTheme.typography.headlineLarge
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    "Nessun post ancora",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = EcoGreen700
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "Sii il primo a condividere qualcosa di verde!",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = EcoGreen600
                                    )
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { refreshData() },
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = EcoGreen600
                                        ),
                                        border = ButtonDefaults.outlinedButtonBorder.copy(
                                            brush = Brush.horizontalGradient(
                                                listOf(EcoGreen400, EcoGreen600)
                                            )
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Refresh,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Riprova")
                                    }

                                    Button(
                                        onClick = {
                                            navController.navigate("group_create_post/$gruppoId")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = EcoGreen500
                                        )
                                    ) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Crea Post")
                                    }
                                }
                            }
                        }
                    }
                }

                // Lista dei post
                items(posts) { post ->
                    PostCard(
                        post = post,
                        commenti = commenti.filter { it.post == post.id },
                        commentViewModel = commentViewModel,
                        authViewModel = authViewModel,
                        token = token
                    )
                }

                // Spazio finale per il FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun PostCard(
    post: PostDto,
    commenti: List<CommentoDto>,
    commentViewModel: CommentViewModel,
    authViewModel: AuthViewModel,
    token: String
) {
    var nuovoCommento by remember { mutableStateOf("") }
    var showComments by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header del post con avatar e info autore
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Avatar placeholder
                    Card(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = EcoGreen100
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = post.autore_username?.take(1)?.uppercase() ?: "U",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EcoGreen600
                                )
                            )
                        }
                    }

                    Column {
                        Text(
                            text = post.autore_username ?: "Utente Sconosciuto",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = EcoGreen800
                            )
                        )
                        Text(
                            text = post.data_pubblicazione?.take(10) ?: "Data non disponibile",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NaturalGray600
                            )
                        )
                    }
                }

                // Indicatore posizione
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = EcoGreen50
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = EcoGreen600,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${post.latitudine ?: 0.0}, ${post.longitudine ?: 0.0}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = EcoGreen600
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenuto del post
            Text(
                text = post.descrizione,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = EcoGreen800,
                    lineHeight = 24.sp
                ),
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            // Pulsante "Leggi di più" se il testo è lungo
            val descrizione = post.descrizione
            if (descrizione.length > 150) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = EcoGreen600
                    )
                ) {
                    Text(
                        if (isExpanded) "Leggi meno" else "Leggi di più",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // Immagine del post se presente
            post.immagine?.let { imageUrl ->
                if (imageUrl.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sezione commenti
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = EcoGreen50
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Header commenti
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Comment,
                                contentDescription = null,
                                tint = EcoGreen600,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                "Commenti (${commenti.size})",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = EcoGreen700
                                )
                            )
                        }

                        if (commenti.isNotEmpty()) {
                            TextButton(
                                onClick = { showComments = !showComments }
                            ) {
                                Text(
                                    if (showComments) "Nascondi" else "Mostra",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = EcoGreen600
                                    )
                                )
                                Icon(
                                    if (showComments) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = EcoGreen600,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // Lista commenti
                    AnimatedVisibility(
                        visible = showComments && commenti.isNotEmpty(),
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            commenti.forEach { commento ->
                                CommentItem(commento)
                            }
                        }
                    }

                    if (commenti.isEmpty()) {
                        Text(
                            "Nessun commento ancora. Sii il primo!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NaturalGray600
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Campo nuovo commento
                    OutlinedTextField(
                        value = nuovoCommento,
                        onValueChange = { nuovoCommento = it },
                        label = { Text("Scrivi un commento...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreen500,
                            focusedLabelColor = EcoGreen500,
                            cursorColor = EcoGreen500,
                            unfocusedBorderColor = EcoGreen300
                        ),
                        trailingIcon = {
                            AnimatedVisibility(visible = nuovoCommento.isNotBlank()) {
                                IconButton(
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
                                                commentViewModel.caricaCommenti(token) {
                                                    var isRefreshing = false
                                                }
                                            }
                                        } else {
                                            Log.d("GroupFeedScreen", "Condizioni non soddisfatte per inviare commento")
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Send,
                                        contentDescription = "Invia commento",
                                        tint = EcoGreen500
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(commento: CommentoDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Avatar commento
            Card(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = EcoGreen200
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = commento.autore_username.take(1).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = EcoGreen700
                        )
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = commento.autore_username,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = EcoGreen700
                    )
                )
                Text(
                    text = commento.testo,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = EcoGreen800
                    )
                )
            }
        }
    }
}