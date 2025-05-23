package com.example.frontend_happygreen.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.data.model.GruppoDto
import com.example.frontend_happygreen.viewmodel.GroupViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSearchScreen(
    navController: NavController,
    token: String,
) {
    val groupViewModel: GroupViewModel = viewModel()
    val gruppi by groupViewModel.gruppi.collectAsState()
    val mieiGruppi by groupViewModel.mieiGruppi.collectAsState()
    val iscrizioneSuccess by groupViewModel.iscrizioneSuccess.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var hasLoadedData by remember { mutableStateOf(false) }

    // Filtra i gruppi in base alla ricerca ed esclude quelli di cui fa già parte
    val gruppiFiltrati = remember(gruppi, mieiGruppi, searchQuery) {
        val mieiGruppiIds = mieiGruppi.map { it.id }.toSet()
        val gruppiDisponibili = gruppi.filter { gruppo ->
            val isNotMyGroup = !mieiGruppiIds.contains(gruppo.id)
            isNotMyGroup
        }

        val risultatoFinale = if (searchQuery.isBlank()) {
            gruppiDisponibili
        } else {
            val filtrati = gruppiDisponibili.filter { gruppo ->
                val matches = gruppo.nome.contains(searchQuery, ignoreCase = true) ||
                        gruppo.descrizione.contains(searchQuery, ignoreCase = true) ||
                        gruppo.creatore_username.contains(searchQuery, ignoreCase = true)
                matches
            }
            filtrati
        }
        risultatoFinale
    }

    // Carica tutti i gruppi quando si apre la schermata
    LaunchedEffect(token, hasLoadedData) {
        if (!hasLoadedData) {
            isLoading = true
            groupViewModel.loadAllGroups(token)
            groupViewModel.loadMyGroups(token)
            kotlinx.coroutines.delay(2000)
            groupViewModel.debugCurrentState()

            isLoading = false
            hasLoadedData = true
        }
    }

    LaunchedEffect(token) {
        if (hasLoadedData) {
            groupViewModel.loadAllGroups(token)
            groupViewModel.loadMyGroups(token)
            kotlinx.coroutines.delay(1000)
            groupViewModel.debugCurrentState()
        }
    }

    // Gestisci il risultato dell'iscrizione
    LaunchedEffect(iscrizioneSuccess) {
        when (iscrizioneSuccess) {
            true -> {
                showSuccessDialog = true
                groupViewModel.resetIscrizioneEsito()
                // Ricarica i dati dopo l'iscrizione
                token.let {
                    groupViewModel.loadMyGroups(it)
                }
            }
            false -> {
                showErrorDialog = true
                groupViewModel.resetIscrizioneEsito()
            }
            null -> { /* Nessuna azione */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con bottone indietro
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
            }
            Text(
                "Trova Gruppi",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f)
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Barra di ricerca
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Cerca gruppi...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Cancella")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sezione informativa o statistiche
        if (searchQuery.isEmpty()) {
            // Quando non c'è ricerca, mostra info sui gruppi consigliati
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Recommend,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Gruppi consigliati per te",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            if (gruppiFiltrati.isNotEmpty())
                                "${gruppiFiltrati.size} gruppi disponibili per l'iscrizione"
                            else if (hasLoadedData)
                                "Nessun nuovo gruppo disponibile"
                            else
                                "Caricamento gruppi in corso...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        } else {
            // Statistiche di ricerca
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Risultati: ${gruppiFiltrati.size} gruppi trovati",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                TextButton(
                    onClick = { searchQuery = "" }
                ) {
                    Text("Cancella filtro")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista dei gruppi
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                isLoading -> {
                    // Stato di caricamento
                    items(3) {
                        LoadingGroupCard()
                    }
                }
                gruppiFiltrati.isEmpty() && hasLoadedData -> {
                    // Nessun gruppo disponibile dopo il caricamento
                    if (searchQuery.isNotEmpty()) {
                        // Nessun risultato di ricerca
                        item {
                            EmptySearchState(
                                searchQuery = searchQuery,
                                onClearSearch = { searchQuery = "" },
                                onCreateGroup = { navController.navigate("group_create") }
                            )
                        }
                    } else {
                        // Nessun gruppo disponibile nel database o tutti i gruppi sono già stati joinati
                        item {
                            if (gruppi.isEmpty()) {
                                EmptyGroupsState(
                                    onCreateGroup = { navController.navigate("group_create") }
                                )
                            } else {
                                AllGroupsJoinedState(
                                    onCreateGroup = { navController.navigate("group_create") },
                                    onViewMyGroups = { navController.navigate("my_groups") }
                                )
                            }
                        }
                    }
                }
                else -> {
                    // Mostra i gruppi reali
                    if (searchQuery.isEmpty()) {
                        item {
                            Text(
                                "Gruppi consigliati (${gruppiFiltrati.size})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                    items(gruppiFiltrati) { gruppo ->
                        GroupCard(
                            gruppo = gruppo,
                            onJoinClick = {
                                token.let {
                                    groupViewModel.iscrivitiAGruppo(gruppo.id, it)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog di successo
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50)
                )
            },
            title = { Text("Iscrizione completata!") },
            text = { Text("Ti sei iscritto al gruppo con successo. Ora puoi partecipare alle discussioni e attività.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigateUp()
                    }
                ) {
                    Text("Vai ai miei gruppi")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("Continua a cercare")
                }
            }
        )
    }

    // Dialog di errore
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            icon = {
                Icon(
                    Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Errore nell'iscrizione") },
            text = { Text("Non è stato possibile iscriverti al gruppo. Riprova più tardi.") },
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupCard(
    gruppo: GruppoDto,
    onJoinClick: () -> Unit
) {
    var isJoining by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header del gruppo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Avatar del gruppo
                Card(
                    modifier = Modifier.size(56.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            gruppo.nome.firstOrNull()?.toString()?.uppercase() ?: "G",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Informazioni del gruppo
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        gruppo.nome,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (gruppo.descrizione.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            gruppo.descrizione,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Informazioni creatore e data
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Creato da ${gruppo.creatore_username}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            formatDate(gruppo.data_creazione),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottone di iscrizione
            Button(
                onClick = {
                    isJoining = true
                    onJoinClick()
                },
                enabled = !isJoining,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isJoining) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iscrizione in corso...")
                } else {
                    Icon(Icons.Default.GroupAdd, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iscriviti al Gruppo")
                }
            }
        }
    }

    // Reset dello stato quando l'iscrizione è completata
    LaunchedEffect(isJoining) {
        if (isJoining) {
            kotlinx.coroutines.delay(2000) // Tempo sufficiente per completare la richiesta
            isJoining = false
        }
    }
}

@Composable
private fun AllGroupsJoinedState(
    onCreateGroup: () -> Unit,
    onViewMyGroups: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Ottimo lavoro!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Sei già iscritto a tutti i gruppi disponibili! Controlla i tuoi gruppi o creane uno nuovo.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onViewMyGroups,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Group, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("I miei gruppi")
                }

                Button(
                    onClick = onCreateGroup,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crea gruppo")
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    searchQuery: String,
    onClearSearch: () -> Unit,
    onCreateGroup: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Nessun gruppo trovato per \"$searchQuery\"",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Prova con termini diversi o crea un nuovo gruppo con questo nome!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClearSearch,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancella ricerca")
                }

                Button(
                    onClick = onCreateGroup,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crea gruppo")
                }
            }
        }
    }
}

@Composable
private fun EmptyGroupsState(
    onCreateGroup: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Group,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Nessun gruppo disponibile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Non ci sono ancora gruppi creati. Sii il primo a creare un gruppo per la tua community!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCreateGroup,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crea il Primo Gruppo")
            }
        }
    }
}

@Composable
private fun LoadingGroupCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Avatar placeholder
                Card(
                    modifier = Modifier.size(56.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize())
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Placeholder per nome
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Placeholder per descrizione
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { }

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) { }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) { }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString.split("T")[0] // Fallback: mostra solo la data
    }
}