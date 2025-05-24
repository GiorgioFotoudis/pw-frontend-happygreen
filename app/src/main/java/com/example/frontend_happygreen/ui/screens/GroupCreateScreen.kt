package com.example.frontend_happygreen.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend_happygreen.viewmodel.GroupViewModel
import com.example.frontend_happygreen.ui.theme.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
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
    var isSuccess by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Header con animazione
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it })
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icona grande per gruppo
                    Card(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = EcoGreen500
                        ),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.GroupAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Crea il tuo gruppo",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EcoGreen800
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Unisci le persone per fare la differenza insieme",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = EcoGreen600
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Dettagli del gruppo",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = EcoGreen700
                        ),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Nome gruppo Field
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome del gruppo") },
                        placeholder = { Text("es. Eco Warriors") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Group,
                                contentDescription = null,
                                tint = EcoGreen500
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreen500,
                            focusedLabelColor = EcoGreen500,
                            cursorColor = EcoGreen500
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        supportingText = {
                            Text(
                                text = "${nome.length}/50",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (nome.length > 40) WarningAmber else EcoGreen400
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descrizione Field
                    OutlinedTextField(
                        value = descrizione,
                        onValueChange = { if (it.length <= 200) descrizione = it },
                        label = { Text("Descrizione") },
                        placeholder = { Text("Descrivi la missione del tuo gruppo...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = null,
                                tint = EcoGreen500
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EcoGreen500,
                            focusedLabelColor = EcoGreen500,
                            cursorColor = EcoGreen500
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        maxLines = 4,
                        supportingText = {
                            Text(
                                text = "${descrizione.length}/200",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (descrizione.length > 180) WarningAmber else EcoGreen400
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Create Button
                    Button(
                        onClick = {
                            isLoading = true
                            groupViewModel.creaGruppo(nome.trim(), descrizione.trim(), token) { success ->
                                isLoading = false
                                if (success) {
                                    isSuccess = true
                                    message = "Gruppo creato con successo!"
                                    // Ritarda la navigazione per mostrare il messaggio
                                    kotlinx.coroutines.GlobalScope.launch {
                                        kotlinx.coroutines.delay(1500)
                                        onGroupCreated()
                                    }
                                } else {
                                    isSuccess = false
                                    message = "Errore durante la creazione del gruppo"
                                }
                            }
                        },
                        enabled = !isLoading && nome.trim().isNotBlank() && descrizione.trim().isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EcoGreen500,
                            disabledContainerColor = EcoGreen300
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Creazione in corso...")
                        } else {
                            Icon(
                                Icons.Default.GroupAdd,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Crea gruppo",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    // Message with animation
                    AnimatedVisibility(visible = message != null) {
                        message?.let { msg ->
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSuccess)
                                        SuccessGreen.copy(alpha = 0.1f)
                                    else
                                        ErrorRed.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isSuccess) "✅" else "❌",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (isSuccess) SuccessGreen else ErrorRed,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Info Card motivazionale
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = EcoGreen50
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💡 Suggerimento",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = EcoGreen700
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Un gruppo attivo e coinvolgente inizia con un nome accattivante e una descrizione chiara degli obiettivi sostenibili",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = EcoGreen600
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer motivazionale
            Text(
                text = "🌱 Ogni gruppo è un passo verso il cambiamento",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = EcoGreen500
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}